package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.entity.LeaveType;
import com.cts.rivio.service.impl.LeaveTypeService; // Make sure this matches your package structure
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    // [LEAV-23] GET all leave rules
    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaveType>>> getLeaveTypes() {
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(ApiResponse.success(leaveTypes, "Leave types fetched successfully"));
    }

    // [LEAV-19] POST create new leave type
    // @Valid ensures the 'Allotment > 0' and 'Name is NotBlank' rules are enforced
    @PostMapping
    public ResponseEntity<ApiResponse<LeaveType>> createLeaveType(@Valid @RequestBody LeaveType leaveType) {
        LeaveType created = leaveTypeService.createLeaveType(leaveType);
        return new ResponseEntity<>(ApiResponse.success(created, "Leave type created successfully"), HttpStatus.CREATED);
    }

    // [LEAV-23] PUT update specific leave rule (Carry-forward or Allotment)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveType>> updateLeaveType(
            @PathVariable Integer id,
            @Valid @RequestBody LeaveType leaveType) {

        LeaveType updated = leaveTypeService.updateLeaveRules(id, leaveType);
        return ResponseEntity.ok(ApiResponse.success(updated, "Leave type updated successfully"));
    }

    /**
     * Delete a Leave Type (Safe Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeaveType(@PathVariable Integer id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Leave Type successfully deleted."));
    }
}