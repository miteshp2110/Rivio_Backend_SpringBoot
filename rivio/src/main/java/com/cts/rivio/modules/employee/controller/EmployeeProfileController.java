package com.cts.rivio.modules.employee.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.employee.dto.request.EmployeeProfileRequest;
import com.cts.rivio.modules.employee.dto.response.EmployeeProfileResponse;
import com.cts.rivio.modules.employee.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> onboardEmployee(@Valid @RequestBody EmployeeProfileRequest request) {
        EmployeeProfileResponse responseData = employeeService.createProfile(request);
        return new ResponseEntity<>(ApiResponse.success(responseData, "Employee profile successfully created"), HttpStatus.CREATED);
    }
}