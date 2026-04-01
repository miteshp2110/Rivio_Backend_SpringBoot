package com.cts.rivio.modules.leave.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.leave.dto.request.LeaveStatusUpdateRequest;
import com.cts.rivio.modules.leave.dto.response.LeaveRequestResponse;
import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import com.cts.rivio.modules.leave.service.LeaveRequestStatusService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> updateLeaveStatus(
            @PathVariable Integer id,
            @Valid @RequestBody LeaveStatusUpdateRequest requestBody,
            @RequestHeader(value = "X-Manager-Id", defaultValue = "1") Integer managerId) {

        LeaveRequestResponse updated = statusService.updateStatus(id, requestBody.getStatus(), managerId);

        return ResponseEntity.ok(ApiResponse.success(updated, "Leave request status updated to " + requestBody.getStatus()));
    }
}