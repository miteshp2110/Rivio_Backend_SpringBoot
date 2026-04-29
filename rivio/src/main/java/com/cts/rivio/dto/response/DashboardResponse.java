package com.cts.rivio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // KPIs
    private long totalEmployees;
    private long newHiresThisMonth;
    private long presentToday;
    private long onLeaveToday;
    private long pendingLeaveRequests;
    private long activePayCycles;

    // Charts
    private List<DepartmentDistribution> departmentDistribution;
    private List<AttendanceTrend> attendanceTrend;

    // Quick Actions
    private List<PendingLeaveDto> recentPendingLeaves;

    // --- Inner Classes for Complex Data Arrays ---

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentDistribution {
        private String departmentName;
        private long employeeCount;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttendanceTrend {
        private String date;
        private double presentRate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PendingLeaveDto {
        private Integer id;
        private String employeeName;
        private String leaveTypeName;
        private Double daysRequested;
    }
}