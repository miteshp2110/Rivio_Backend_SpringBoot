package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.response.LeaveBalanceResponse;
import com.cts.rivio.mapper.LeaveBalanceMapper;
import com.cts.rivio.service.impl.LeaveBalanceService;
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
    private LeaveBalanceMapper mapper;

    @GetMapping("/{id}/leave-balances")
    public ResponseEntity<ApiResponse<List<LeaveBalanceResponse>>> getEmployeeBalances(
            @PathVariable("id") Integer employeeId,
            @RequestParam(required = false) Integer year) {

        // Fetch raw entities
        List<LeaveBalanceResponse> balances = leaveBalanceService.getBalancesByEmployee(employeeId, year)
                .stream()
                .map(mapper::toResponse) // Map to safe DTOs
                .collect(Collectors.toList());

        // Wrap in standard ApiResponse
        return ResponseEntity.ok(ApiResponse.success(balances, "Leave balances fetched successfully"));
    }
}