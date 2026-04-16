package com.cts.rivio.dto.response;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long totalActiveEmployees;
    private long totalOpenJobs;
    private long pendingLeaveRequests;
    private long totalCandidates;
}