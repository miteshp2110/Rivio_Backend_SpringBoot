package com.cts.rivio.service.impl;

import com.cts.rivio.dto.request.LeaveRequestPayload;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.entity.*;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.mapper.LeaveRequestMapper;
import com.cts.rivio.repository.*;
import com.cts.rivio.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private EmployeeProfileRepository employeeRepository;
    @Autowired private LeaveTypeRepository leaveTypeRepository;
    @Autowired private EmployeeLeaveBalanceRepository balanceRepository;
    @Autowired private LeaveRequestMapper mapper;


    @Override
    public List<LeaveRequestResponse> getPendingRequestsForManager(Integer managerId) {
        // Fetch only PENDING requests for employees reporting to this manager
        List<LeaveRequest> pendingRequests = leaveRequestRepository
                .findByStatusAndManagerId(LeaveStatus.PENDING, managerId);

        // Map the entities to our detailed DTOs
        return pendingRequests.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LeaveRequestResponse updateLeaveStatus(Integer requestId, LeaveStatus newStatus, Integer managerId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave Request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Request has already been processed");
        }

        EmployeeProfile manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        request.setStatus(newStatus);
        request.setApprovedBy(manager);
        return mapper.toResponse(leaveRequestRepository.save(request));
    }

    @Override
    @Transactional
    public LeaveRequestResponse submitRequest(LeaveRequestPayload payload) {
        EmployeeProfile employee = employeeRepository.findById(payload.getEmployeeProfileId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveType leaveType = leaveTypeRepository.findById(payload.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));

        LeaveRequest request = LeaveRequest.builder()
                .employee(employee)
                .leaveType(leaveType)
                .startDate(payload.getStartDate())
                .endDate(payload.getEndDate())
                .daysRequested(BigDecimal.valueOf(payload.getDaysRequested()))
                .status(LeaveStatus.PENDING)
                .build();

        return mapper.toResponse(leaveRequestRepository.save(request));
    }

    @Override
    public List<LeaveRequestResponse> getEmployeeLeaveRequests(Integer employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderByStartDateDesc(employeeId)
                .stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLeaveRequest(Integer requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be deleted");
        }
        leaveRequestRepository.delete(request);
    }

    @Override
    public List<LeaveRequestResponse> getPendingRequests(){
        List<LeaveRequest> pendingRequests = leaveRequestRepository.findAll();
        return pendingRequests.stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}