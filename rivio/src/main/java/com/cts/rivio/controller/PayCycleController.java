package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.request.PayCycleStatusUpdateRequest;
import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.dto.response.PaySlipResponse;
import com.cts.rivio.service.PayCycleService;
import com.cts.rivio.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pay-cycles")
@RequiredArgsConstructor
public class PayCycleController {

    private final PayCycleService payCycleService;
    private final PayrollService payrollService;

    /**
     * [PAY-42] Create a new Pay Cycle
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PayCycleResponse>> createPayCycle(@Valid @RequestBody PayCycleRequest request) {
        PayCycleResponse response = payCycleService.createPayCycle(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Pay Cycle created successfully in Draft mode"), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PayCycleResponse>>> getAllPayCycles(
            @RequestParam(required = false) String name) {

        List<PayCycleResponse> responses = payCycleService.getAllPayCycles(name);
        return ResponseEntity.ok(ApiResponse.success(responses, "Pay cycles fetched successfully"));
    }

    @PostMapping("/{id}/generate-payslips")
    public ResponseEntity<ApiResponse<List<PaySlipResponse>>> generatePayslips(@PathVariable Integer id) {
        List<PaySlipResponse> generatedSlips = payrollService.generatePayslipsForCycle(id);
        return ResponseEntity.ok(ApiResponse.success(
                generatedSlips,
                "Successfully generated " + generatedSlips.size() + " payslips. Pay Cycle is now in PROCESSING state."
        ));
    }

    /**
     * Get all generated payslips for a specific Pay Cycle
     */
    @GetMapping("/{id}/payslips")
    public ResponseEntity<ApiResponse<List<PaySlipResponse>>> getPayslipsByPayCycle(@PathVariable Integer id) {
        List<PaySlipResponse> responses = payrollService.getPayslipsByPayCycle(id);
        return ResponseEntity.ok(ApiResponse.success(responses, "Payslips fetched successfully"));
    }

    /**
     * Get a specific employee's payslip for a specific Pay Cycle
     */
    @GetMapping("/{id}/payslips/employee/{employeeId}")
    public ResponseEntity<ApiResponse<PaySlipResponse>> getPayslipForEmployee(
            @PathVariable Integer id,
            @PathVariable Integer employeeId) {
        PaySlipResponse response = payrollService.getPayslipByEmployeeAndPayCycle(id, employeeId);
        return ResponseEntity.ok(ApiResponse.success(response, "Employee payslip fetched successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PayCycleResponse>> updatePayCycleStatus(
            @PathVariable Integer id,
            @Valid @RequestBody PayCycleStatusUpdateRequest request) {

        PayCycleResponse response = payCycleService.updateStatus(id, request.getStatus());

        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Pay cycle status successfully transitioned to " + request.getStatus()
        ));
    }

}