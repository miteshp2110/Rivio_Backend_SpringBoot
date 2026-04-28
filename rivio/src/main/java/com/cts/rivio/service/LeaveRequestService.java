package com.cts.rivio.service;

import com.cts.rivio.dto.request.LeaveRequestPayload;
import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.enums.LeaveStatus;
import java.util.List;

public interface LeaveRequestService {
    // For Managers to see team requests
    List<LeaveRequestResponse> getPendingRequestsForManager(Integer managerId);


    // For Managers to Approve/Reject
    LeaveRequestResponse updateLeaveStatus(Integer requestId, LeaveStatus newStatus, Integer managerId);

    // For Employees to submit
    LeaveRequestResponse submitRequest(LeaveRequestPayload payload);

    // For Employee History and HR Admin Audit
    List<LeaveRequestResponse> getEmployeeLeaveRequests(Integer employeeId);

    // For Employees to cancel/delete
    void deleteLeaveRequest(Integer requestId);
}