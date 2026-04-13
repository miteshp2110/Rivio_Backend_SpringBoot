package com.cts.rivio.service.impl;

import com.cts.rivio.dto.request.LeaveRequestPayload;
import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.entity.EmployeeLeaveBalance;
import com.cts.rivio.entity.LeaveRequest;
import com.cts.rivio.entity.LeaveType;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.mapper.LeaveRequestMapper;
import com.cts.rivio.repository.EmployeeLeaveBalanceRepository;
import com.cts.rivio.repository.LeaveRequestRepository;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeLeaveBalanceRepository balanceRepository;

    @Autowired private EmployeeProfileRepository employeeRepository;
    @Autowired private LeaveTypeRepository leaveTypeRepository;
    @Autowired private LeaveRequestMapper mapper;

    public List<LeaveRequestResponse> getManagerPendingRequests(Integer managerId, LeaveStatus status) {
        // AC 1: Support status filtering (Default: Pending)
        LeaveStatus filterStatus = (status != null) ? status : LeaveStatus.PENDING;

        return leaveRequestRepository.findByManagerIdAndStatus(managerId, filterStatus)
                .stream()
                .map(mapper::toResponse) // AC 2: DTO automatically includes Employee Name and Leave Type Name
                .collect(Collectors.toList());
    }

    @Transactional
    public LeaveRequestResponse submitRequest(
            LeaveRequestPayload payload) {

        // 1. AC-2: Validate Date Range
        if (payload.getStartDate().isAfter(payload.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        // 2. Fetch Entities
        EmployeeProfile employee = employeeRepository.findById(payload.getEmployeeProfileId())
                .orElseThrow(() -> new com.cts.rivio.core.exception.ResourceNotFoundException("Employee", "id", payload.getEmployeeProfileId()));

        LeaveType leaveType = leaveTypeRepository.findById(payload.getLeaveTypeId())
                .orElseThrow(() -> new com.cts.rivio.core.exception.ResourceNotFoundException("Leave Type", "id", payload.getLeaveTypeId()));

        // 3. AC-1: Sufficient Balance Check
        int year = payload.getStartDate().getYear();
        EmployeeLeaveBalance balance = balanceRepository.findByEmployeeProfileIdAndLeaveTypeIdAndYear(
                employee.getId(),
                leaveType.getId(),
                year
        ).orElseThrow(() -> new IllegalArgumentException("No leave balance record found for this year. Please ask HR to run the allocation process."));

        BigDecimal requestedDays = BigDecimal.valueOf(payload.getDaysRequested());

        // Using your @Formula 'balance' (allotted - consumed)
        if (balance.getBalance().compareTo(requestedDays) < 0) {
            throw new IllegalArgumentException("Insufficient balance. Available: " + balance.getBalance() + " days.");
        }

        // 4. Build Entity & Force status to PENDING
        LeaveRequest request = LeaveRequest.builder()
                .employee(employee)
                .leaveType(leaveType)
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .daysRequested(requestedDays)
                .status(LeaveStatus.PENDING)
                .build();

        request = leaveRequestRepository.save(request);

        return mapper.toResponse(request);
    }
}