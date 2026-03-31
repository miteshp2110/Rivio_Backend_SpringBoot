package com.cts.rivio.modules.leave.service;

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import com.cts.rivio.modules.leave.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.modules.leave.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LeaveRequestStatusService {

    @Autowired private LeaveRequestRepository requestRepository;
    @Autowired private EmployeeLeaveBalanceRepository balanceRepository;

    @Transactional
    public LeaveRequest updateStatus(Integer requestId, LeaveStatus newStatus, EmployeeProfile manager) {
        // 1. Fetch the request
        LeaveRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave Request not found"));

        // 2. Prevent duplicate processing
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Request is already " + request.getStatus());
        }

        // 3. If Approved, update the balance
        if (newStatus == LeaveStatus.APPROVED) {
            deductBalance(request);
        }

        // 4. Update request details (LEAV-25 AC2)
        request.setStatus(newStatus);
        request.setApprovedBy(manager); // Assuming you have this field in LeaveRequest

        return requestRepository.save(request);
    }

    private void deductBalance(LeaveRequest request) {
        int year = request.getStartDate().getYear();

        EmployeeLeaveBalance balance = balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(
                request.getEmployee().getId(),
                request.getLeaveType().getId(),
                year
        ).orElseThrow(() -> new RuntimeException("No leave balance found for this year"));

        // Calculate duration (simple days check)
        long days = java.time.temporal.ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal leaveDays = BigDecimal.valueOf(days);

        // Update consumed count
        balance.setConsumed(balance.getConsumed().add(leaveDays));
        balanceRepository.save(balance);
    }
}