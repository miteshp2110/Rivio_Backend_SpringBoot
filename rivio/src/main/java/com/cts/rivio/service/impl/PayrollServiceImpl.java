package com.cts.rivio.service.impl;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.response.PaySlipResponse;
import com.cts.rivio.entity.*;
import com.cts.rivio.enums.*;
import com.cts.rivio.mapper.PaySlipMapper;
import com.cts.rivio.repository.*;
import com.cts.rivio.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayCycleRepository payCycleRepository;
    private final EmployeeProfileRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    private final WorkDayRepository workDayRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaySlipRepository paySlipRepository;
    private final PaySlipMapper paySlipMapper;

    @Override
    @Transactional
    public List<PaySlipResponse> generatePayslipsForCycle(Integer payCycleId) {
        PayCycle cycle = payCycleRepository.findById(payCycleId)
                .orElseThrow(() -> new ResourceNotFoundException("PayCycle", "id", payCycleId));

        // Validation: Only generate if Draft or Processing
        if (cycle.getStatus() == PayCycleStatus.FINALIZED || cycle.getStatus() == PayCycleStatus.PAID) {
            throw new IllegalStateException("Cannot regenerate payslips for a cycle that is already Finalized or Paid.");
        }

        // 1. Calculate Total Working Days in the Cycle
        int totalWorkingDays = calculateTotalWorkingDays(cycle.getFromDate(), cycle.getToDate());
        if (totalWorkingDays == 0) {
            throw new IllegalStateException("There are 0 working days in this pay cycle duration.");
        }

        // 2. Fetch all Active Employees
        List<EmployeeProfile> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        // 3. Clear any existing draft payslips for this cycle to prevent duplicates
        paySlipRepository.deleteByPayCycleId(cycle.getId());

        List<PaySlip> generatedPayslips = new ArrayList<>();

        // 4. Process Payroll for Each Employee
        for (EmployeeProfile emp : activeEmployees) {

            // A. Calculate Fixed Monthly Totals from Salary Components
            BigDecimal monthlyEarnings = BigDecimal.ZERO;
            BigDecimal monthlyDeductions = BigDecimal.ZERO;

            for (SalaryComponent comp : emp.getSalaryComponents()) {
                if (comp.getType() == ComponentType.EARNING) {
                    monthlyEarnings = monthlyEarnings.add(comp.getValue());
                } else {
                    monthlyDeductions = monthlyDeductions.add(comp.getValue());
                }
            }

            // B. Calculate Per-Day Values (Rounded to 2 decimal places)
            BigDecimal perDayEarning = monthlyEarnings.divide(BigDecimal.valueOf(totalWorkingDays), 2, RoundingMode.HALF_UP);
            BigDecimal perDayDeduction = monthlyDeductions.divide(BigDecimal.valueOf(totalWorkingDays), 2, RoundingMode.HALF_UP);

            // C. Fetch Attendance (Payable Days = Present + Paid Leave)
            int payableDays = attendanceRepository.countByEmployeeProfileIdAndDateBetweenAndStatusIn(
                    emp.getId(),
                    cycle.getFromDate(),
                    cycle.getToDate(),
                    List.of(AttendanceStatus.PRESENT, AttendanceStatus.LEAVE)
            );

            // Cap payable days to total working days (just in case they worked on a weekend/holiday)
            payableDays = Math.min(payableDays, totalWorkingDays);

            // D. Calculate Actual Payout
            BigDecimal actualEarnings = perDayEarning.multiply(BigDecimal.valueOf(payableDays));
            BigDecimal actualDeductions = perDayDeduction.multiply(BigDecimal.valueOf(payableDays));
            BigDecimal netPay = actualEarnings.subtract(actualDeductions);

            // E. Build the Payslip
            PaySlip slip = PaySlip.builder()
                    .payCycle(cycle)
                    .employeeProfile(emp)
                    .grossEarnings(actualEarnings)
                    .totalDeductions(actualDeductions)
                    .netPay(netPay)
                    .build();

            generatedPayslips.add(slip);
        }

        // 5. Save all Payslips and Update Cycle Status
        paySlipRepository.saveAll(generatedPayslips);
        cycle.setStatus(PayCycleStatus.PROCESSING);
        payCycleRepository.save(cycle);

        // Return generated data to frontend
        return generatedPayslips.stream()
                .map(paySlipMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper to calculate true working days (excluding holidays and weekends)
     */
    private int calculateTotalWorkingDays(LocalDate startDate, LocalDate endDate) {
        // Fetch configs
        List<Holiday> holidays = holidayRepository.findByDateBetween(startDate, endDate);
        List<LocalDate> holidayDates = holidays.stream().map(Holiday::getDate).toList();

        List<WorkDay> allWorkDays = workDayRepository.findAll();
        Map<String, Boolean> workDayMap = allWorkDays.stream()
                .collect(Collectors.toMap(
                        w -> w.getDayName().toUpperCase(),
                        WorkDay::getIsWorkingDay
                ));

        int workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            String dayName = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
            boolean isWeekend = !workDayMap.getOrDefault(dayName, true);
            boolean isHoliday = holidayDates.contains(currentDate);

            // If it's a normal working day and NOT a holiday
            if (!isWeekend && !isHoliday) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    @Override
    public List<PaySlipResponse> getPayslipsByPayCycle(Integer payCycleId) {
        // Validate the cycle exists before querying
        if (!payCycleRepository.existsById(payCycleId)) {
            throw new ResourceNotFoundException("Pay Cycle", "id", payCycleId);
        }

        return paySlipRepository.findByPayCycleId(payCycleId)
                .stream()
                .map(paySlipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaySlipResponse getPayslipByEmployeeAndPayCycle(Integer payCycleId, Integer employeeId) {
        PaySlip paySlip = paySlipRepository.findByPayCycleIdAndEmployeeProfileId(payCycleId, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PaySlip", "payCycleId " + payCycleId + " and employeeId", employeeId));

        return paySlipMapper.toResponse(paySlip);
    }


}