package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import com.cts.rivio.modules.leave.service.LeaveRequestStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/leave-requests")
public class LeaveStatusController {

    @Autowired
    private LeaveRequestStatusService statusService;

    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> requestBody) {

        LeaveStatus newStatus = LeaveStatus.valueOf(requestBody.get("status").toUpperCase());

        // Note: In a real app, 'manager' would come from the Security Context (JWT)
        // For now, we are focusing on the transition logic
        LeaveRequest updated = statusService.updateStatus(id, newStatus, null);

        return ResponseEntity.ok(updated);
    }
}