package com.cts.rivio.service.impl;

import com.cts.rivio.dto.response.DashboardResponse;
import com.cts.rivio.entity.Attendance;
import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.entity.LeaveRequest;
import com.cts.rivio.enums.AttendanceStatus;
import com.cts.rivio.enums.EmployeeStatus;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.enums.PayCycleStatus;
import com.cts.rivio.repository.*;
import com.cts.rivio.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeProfileRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayCycleRepository payCycleRepository;

    @Override
    public DashboardResponse getAdminSummary() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // 1. Basic KPIs
        List<EmployeeProfile> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        long totalEmployees = activeEmployees.size();

        long newHires = employeeRepository.countByJoiningDateBetween(startOfMonth, today);
        long presentToday = attendanceRepository.countByDateAndStatus(today, AttendanceStatus.PRESENT);
        long onLeaveToday = attendanceRepository.countByDateAndStatus(today, AttendanceStatus.LEAVE);

        List<LeaveRequest> allPendingLeaves = leaveRequestRepository.findAll().stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .collect(Collectors.toList());

        long activePayCycles = payCycleRepository.countByStatusIn(
                List.of(PayCycleStatus.DRAFT, PayCycleStatus.PROCESSING, PayCycleStatus.FINALIZED)
        );

        // 2. Department Distribution Chart Data
        Map<String, Long> deptCounts = activeEmployees.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDepartment().getName(),
                        Collectors.counting()
                ));

        List<DashboardResponse.DepartmentDistribution> deptDistribution = deptCounts.entrySet().stream()
                .map(entry -> new DashboardResponse.DepartmentDistribution(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 3. Attendance Trend Chart Data (Last 7 Days)
        LocalDate sevenDaysAgo = today.minusDays(6); // 6 days ago + today = 7 days
        List<Attendance> last7DaysAttendance = attendanceRepository.findByDateBetween(sevenDaysAgo, today);

        List<DashboardResponse.AttendanceTrend> attendanceTrend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long presentCount = last7DaysAttendance.stream()
                    .filter(a -> a.getDate().equals(date) && a.getStatus() == AttendanceStatus.PRESENT)
                    .count();

            // Calculate percentage safely
            double rate = totalEmployees > 0 ? ((double) presentCount / totalEmployees) * 100 : 0.0;

            attendanceTrend.add(new DashboardResponse.AttendanceTrend(
                    date.format(formatter),
                    Math.round(rate * 10.0) / 10.0 // Round to 1 decimal place
            ));
        }

        // 4. Quick Actions (Recent 5 Pending Leaves)
        List<DashboardResponse.PendingLeaveDto> recentLeaves = allPendingLeaves.stream()
                .limit(5)
                .map(lr -> DashboardResponse.PendingLeaveDto.builder()
                        .id(lr.getId())
                        .employeeName(lr.getEmployee().getFirstName() + " " + lr.getEmployee().getLastName())
                        .leaveTypeName(lr.getLeaveType().getName())
                        .daysRequested(lr.getDaysRequested().doubleValue())
                        .build())
                .collect(Collectors.toList());

        // 5. Build and Return
        return DashboardResponse.builder()
                .totalEmployees(totalEmployees)
                .newHiresThisMonth(newHires)
                .presentToday(presentToday)
                .onLeaveToday(onLeaveToday)
                .pendingLeaveRequests(allPendingLeaves.size())
                .activePayCycles(activePayCycles)
                .departmentDistribution(deptDistribution)
                .attendanceTrend(attendanceTrend)
                .recentPendingLeaves(recentLeaves)
                .build();
    }
}