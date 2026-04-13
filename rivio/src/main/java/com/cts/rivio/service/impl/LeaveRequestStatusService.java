package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.entity.EmployeeLeaveBalance;
import com.cts.rivio.entity.LeaveRequest;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.mapper.LeaveRequestMapper;
import com.cts.rivio.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LeaveRequestStatusService {

    @Autowired private LeaveRequestRepository requestRepository;
    @Autowired private EmployeeLeaveBalanceRepository balanceRepository;
    @Autowired private EmployeeProfileRepository employeeRepository;
    @Autowired private LeaveRequestMapper mapper;

    @Transactional // AC 1: Balance update and status change must succeed or fail together
    public LeaveRequestResponse updateStatus(Integer requestId, LeaveStatus newStatus, Integer managerId) {

        // 1. Fetch the request
        LeaveRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Request", "id", requestId));

        // 2. Fetch the Manager
        EmployeeProfile manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager Profile", "id", managerId));

        // 3. Prevent duplicate processing
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("This request has already been marked as " + request.getStatus());
        }

        // 4. If Approved, update the balance
        if (newStatus == LeaveStatus.APPROVED) {
            deductBalance(request);
        }

        // 5. AC 2: Update request details & record approved_by_profile_id
        request.setStatus(newStatus);
        request.setApprovedBy(manager);

        request = requestRepository.save(request);
        return mapper.toResponse(request);
    }

    private void deductBalance(LeaveRequest request) {
        int year = request.getStartDate().getYear();

        EmployeeLeaveBalance balance = balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(
                request.getEmployee().getId(),
                request.getLeaveType().getId(),
                year
        ).orElseThrow(() -> new IllegalArgumentException("No leave balance found for this year"));

        BigDecimal leaveDays = request.getDaysRequested();

        // Failsafe: Ensure they didn't run out of balance between applying and approval
        if (balance.getBalance().compareTo(leaveDays) < 0) {
            throw new IllegalArgumentException("Employee does not have enough balance remaining to approve this leave.");
        }

        // Increment consumed count
        balance.setConsumed(balance.getConsumed().add(leaveDays));
        balanceRepository.save(balance);
    }
}