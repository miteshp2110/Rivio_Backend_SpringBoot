package com.cts.rivio.modules.leave.service;

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.employee.enums.EmployeeStatus;
import com.cts.rivio.modules.employee.repository.EmployeeProfileRepository;
import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import com.cts.rivio.modules.leave.entity.LeaveType;
import com.cts.rivio.modules.leave.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.modules.leave.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LeaveAllocationService {

    @Autowired private EmployeeProfileRepository employeeRepository;
    @Autowired private LeaveTypeRepository leaveTypeRepository;
    @Autowired private EmployeeLeaveBalanceRepository balanceRepository;

    @Transactional
    public void allocateBalancesForYear(Integer year) {

        // FIX: Only fetch ACTIVE employees, excluding Terminated/Suspended
        List<EmployeeProfile> employees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (EmployeeProfile employee : employees) {
            for (LeaveType type : leaveTypes) {

                // 1. Idempotency Check (LEAV-21 AC1)
                // If a record already exists for this employee, leave type, and year, skip it.
                if (balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(
                        employee.getId(), type.getId(), year).isPresent()) {
                    continue;
                }

                // 2. Carry-forward Logic (LEAV-21 AC2)
                BigDecimal carryForwardAmount = calculateCarryForward(employee.getId(), type, year - 1);

                // 3. Create New Balance Record
                BigDecimal yearlyAllotment = type.getYearlyAllotment() != null ? type.getYearlyAllotment() : BigDecimal.ZERO;

                EmployeeLeaveBalance newBalance = EmployeeLeaveBalance.builder()
                        .employeeProfile(employee)
                        .leaveType(type)
                        .year(year)
                        .allotted(yearlyAllotment.add(carryForwardAmount))
                        .consumed(BigDecimal.ZERO)
                        .build();

                balanceRepository.save(newBalance);
            }
        }
    }

    private BigDecimal calculateCarryForward(Integer employeeId, LeaveType type, Integer prevYear) {
        return balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(employeeId, type.getId(), prevYear)
                .map(prevBalance -> {
                    BigDecimal remaining = prevBalance.getBalance(); // From @Formula
                    BigDecimal limit = type.getCarryForwardLimit();

                    if (remaining == null || remaining.compareTo(BigDecimal.ZERO) <= 0) {
                        return BigDecimal.ZERO;
                    }

                    return remaining.min(limit != null ? limit : BigDecimal.ZERO);
                })
                .orElse(BigDecimal.ZERO);
    }
}