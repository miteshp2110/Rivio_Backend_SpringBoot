package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.modules.leave.entity.LeaveType;
import com.cts.rivio.modules.leave.service.LeaveTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-types")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    // [LEAV-23] GET all leave rules
    @GetMapping
    public ResponseEntity<List<LeaveType>> getLeaveTypes() {
        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
    }

    // [LEAV-19] POST create new leave type
    // @Valid ensures the 'Allotment > 0' and 'Name is NotBlank' rules are enforced
    @PostMapping
    public ResponseEntity<LeaveType> createLeaveType(@Valid @RequestBody LeaveType leaveType) {
        LeaveType created = leaveTypeService.createLeaveType(leaveType);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // [LEAV-23] PUT update specific leave rule (Carry-forward or Allotment)
    @PutMapping("/{id}")
    public ResponseEntity<LeaveType> updateLeaveType(
            @PathVariable Integer id,
            @Valid @RequestBody LeaveType leaveType) {

        LeaveType updated = leaveTypeService.updateLeaveRules(id, leaveType);
        return ResponseEntity.ok(updated);
    }
}