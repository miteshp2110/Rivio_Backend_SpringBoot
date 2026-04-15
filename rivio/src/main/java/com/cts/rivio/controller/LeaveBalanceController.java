package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.response.LeaveBalanceResponse;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.mapper.LeaveBalanceMapper;
import com.cts.rivio.service.impl.LeaveBalanceService;
import com.cts.rivio.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private LeaveBalanceMapper mapper;

    @GetMapping("/{id}/leave-balances")
    public ResponseEntity<ApiResponse<List<LeaveBalanceResponse>>> getEmployeeBalances(
            @PathVariable("id") Integer employeeId,
            @RequestParam(required = false) Integer year) {

        List<LeaveBalanceResponse> balances = leaveBalanceService.getBalancesByEmployee(employeeId, year)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(balances, "Leave balances fetched successfully"));
    }

    @GetMapping("/{id}/leave-requests")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getEmployeeLeaveHistory(
            @PathVariable("id") Integer employeeId) {

        List<LeaveRequestResponse> history = leaveRequestService.getEmployeeLeaveRequests(employeeId);
        return ResponseEntity.ok(ApiResponse.success(history, "Leave history fetched successfully"));
    }
}