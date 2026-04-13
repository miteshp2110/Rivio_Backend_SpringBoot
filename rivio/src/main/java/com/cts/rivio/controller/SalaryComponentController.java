package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.SalaryComponentRequest;
import com.cts.rivio.dto.response.SalaryComponentResponse;
import com.cts.rivio.service.SalaryComponentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary-components")
@RequiredArgsConstructor
public class SalaryComponentController {

    private final SalaryComponentService salaryComponentService;

    // GET all components for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<SalaryComponentResponse>>> getComponentsByEmployee(@PathVariable Integer employeeId) {
        List<SalaryComponentResponse> responses = salaryComponentService.getComponentsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success(responses, "Salary components fetched successfully"));
    }

    // POST a new component to a specific employee
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<SalaryComponentResponse>> addComponent(
            @PathVariable Integer employeeId,
            @Valid @RequestBody SalaryComponentRequest request) {
        SalaryComponentResponse response = salaryComponentService.addComponent(employeeId, request);
        return new ResponseEntity<>(ApiResponse.success(response, "Salary component added successfully"), HttpStatus.CREATED);
    }

    // PUT to update an existing component
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryComponentResponse>> updateComponent(
            @PathVariable Integer id,
            @Valid @RequestBody SalaryComponentRequest request) {
        SalaryComponentResponse response = salaryComponentService.updateComponent(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Salary component updated successfully"));
    }

    // DELETE a component
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComponent(@PathVariable Integer id) {
        salaryComponentService.deleteComponent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Salary component deleted successfully"));
    }
}