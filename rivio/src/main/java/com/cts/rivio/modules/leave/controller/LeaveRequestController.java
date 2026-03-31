package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import com.cts.rivio.modules.leave.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    /**
     * [LEAV-24] GET /api/leave-requests/pending/{managerId}
     * Fetches requests for a manager's direct reports.
     */
    @GetMapping("/pending/{managerId}")
    public ResponseEntity<List<LeaveRequest>> getPending(
            @PathVariable Integer managerId,
            @RequestParam(required = false) LeaveStatus status) {

        return ResponseEntity.ok(leaveRequestService.getManagerPendingRequests(managerId, status));
    }

    /**
     * [LEAV-20] POST /api/leave-requests
     * Endpoint for employees to submit a new leave request.
     * Includes balance and date validation logic.
     */
    @PostMapping
    public ResponseEntity<LeaveRequest> submitRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest savedRequest = leaveRequestService.submitRequest(leaveRequest);
        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }
}