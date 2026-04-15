package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.LeaveRequestPayload;
import com.cts.rivio.dto.request.LeaveStatusUpdateRequest;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired private LeaveRequestService leaveRequestService;

    // 1. Submit a request
    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> submit(@Valid @RequestBody LeaveRequestPayload payload) {
        return ResponseEntity.ok(ApiResponse.success(leaveRequestService.submitRequest(payload), "Leave request submitted"));
    }

    // 2. Manager Action: Approve or Reject
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody LeaveStatusUpdateRequest payload,
            @RequestHeader(value = "X-Manager-Id") Integer managerId) {
        return ResponseEntity.ok(ApiResponse.success(leaveRequestService.updateLeaveStatus(id, payload.getStatus(), managerId), "Status updated"));
    }

    // 3. HR Admin / Employee history
    @GetMapping("/employee/{id}/history")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(leaveRequestService.getEmployeeLeaveRequests(id), "History fetched"));
    }

    // 4. Employee Action: Delete pending leave
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Leave request deleted successfully"));
    }
}