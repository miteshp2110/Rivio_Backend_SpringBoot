package com.cts.rivio.controller;

import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.service.impl.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.LeaveRequestPayload;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getPending(
            @RequestHeader(value = "X-Manager-Id", defaultValue = "1") Integer managerId,
            @RequestParam(required = false) LeaveStatus status) {

        List<LeaveRequestResponse> pendingRequests = leaveRequestService.getManagerPendingRequests(managerId, status);
        return ResponseEntity.ok(ApiResponse.success(pendingRequests, "Team leave requests fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> submitRequest(@Valid @RequestBody LeaveRequestPayload payload) {
        LeaveRequestResponse responseData = leaveRequestService.submitRequest(payload);
        return new ResponseEntity<>(ApiResponse.success(responseData, "Leave request submitted successfully"), HttpStatus.CREATED);
    }
}