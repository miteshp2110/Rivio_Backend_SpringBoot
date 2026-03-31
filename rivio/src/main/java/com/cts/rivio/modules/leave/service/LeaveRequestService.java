package com.cts.rivio.modules.leave.service;

import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import com.cts.rivio.modules.leave.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.modules.leave.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeLeaveBalanceRepository balanceRepository;

    /**
     * [LEAV-24] Fetches leave requests for a specific manager's direct reports.
     */
    public List<LeaveRequest> getManagerPendingRequests(Integer managerId, LeaveStatus status) {
        LeaveStatus filterStatus = (status != null) ? status : LeaveStatus.PENDING;
        return leaveRequestRepository.findByManagerIdAndStatus(managerId, filterStatus);
    }

    /**
     * [LEAV-20] Submits a new leave request with validation.
     */
    @Transactional
    public LeaveRequest submitRequest(LeaveRequest request) {
        // 1. AC-2: Validate Date Range
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        // 2. Calculate requested days (inclusive)
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal requestedDays = BigDecimal.valueOf(days);

        // 3. AC-1: Sufficient Balance Check
        int year = request.getStartDate().getYear();
        EmployeeLeaveBalance balance = balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(
                request.getEmployee().getId(),
                request.getLeaveType().getId(),
                year
        ).orElseThrow(() -> new RuntimeException("No leave balance record found for this year."));

        // Using your @Formula 'balance' (allotted - consumed)
        if (balance.getBalance().compareTo(requestedDays) < 0) {
            throw new RuntimeException("Insufficient balance. Available: " + balance.getBalance() + ", Requested: " + days);
        }

        // 4. Force status to PENDING regardless of what the frontend sent
        request.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepository.save(request);
    }
}