package com.cts.rivio.modules.company.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.company.dto.request.DesignationRequest;
import com.cts.rivio.modules.company.dto.response.DesignationResponse;
import com.cts.rivio.modules.company.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/designations")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    @PostMapping
    public ResponseEntity<ApiResponse<DesignationResponse>> createDesignation(@RequestBody DesignationRequest request) {
        DesignationResponse response = designationService.createDesignation(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Designation created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DesignationResponse>>> getAllDesignations(
            @RequestParam(required = false) Integer departmentId) {
        List<DesignationResponse> response = designationService.getAllDesignations(departmentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Designations fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DesignationResponse>> updateDesignation(
            @PathVariable Integer id,
            @RequestBody DesignationRequest request) {
        DesignationResponse response = designationService.updateDesignation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Designation updated successfully"));
    }
}