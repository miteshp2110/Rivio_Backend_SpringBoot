package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.EmployeeProfileRequest;
import com.cts.rivio.dto.request.EmployeeStatusUpdateRequest;
import com.cts.rivio.dto.request.JobDetailsUpdateRequest;
import com.cts.rivio.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.dto.response.EmployeeProfileResponse;
import com.cts.rivio.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> getProfileById(
            @PathVariable Integer id,
            @RequestHeader(value = "X-Role", defaultValue = "Employee") String requesterRole) {

        EmployeeProfileResponse profile = employeeService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile fetched successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeDirectoryResponse>>> getEmployeeDirectory(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeDirectoryResponse> directory = employeeService.getEmployeeDirectory(search, page, size);
        return ResponseEntity.ok(ApiResponse.success(directory, "Employee directory fetched successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> updateEmployeeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeStatusUpdateRequest request) {

        EmployeeProfileResponse updatedProfile = employeeService.updateEmployeeStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Employee status updated successfully"));
    }
    @PutMapping("/{id}/job-details")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> updateJobDetails(
            @PathVariable Integer id,
            @Valid @RequestBody JobDetailsUpdateRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Integer changedByUserId) {

        EmployeeProfileResponse updatedProfile = employeeService.updateJobDetails(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Job details updated successfully"));
    }
}