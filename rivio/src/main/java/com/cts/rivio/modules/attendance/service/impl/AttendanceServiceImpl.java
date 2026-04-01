package com.cts.rivio.modules.attendance.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.attendance.dto.request.AttendanceRequest;
import com.cts.rivio.modules.attendance.dto.request.PunchOutRequest;
import com.cts.rivio.modules.attendance.dto.response.AttendanceResponse;
import com.cts.rivio.modules.attendance.entity.Attendance;
import com.cts.rivio.modules.attendance.entity.WorkDay;
import com.cts.rivio.modules.attendance.enums.AttendanceStatus;
import com.cts.rivio.modules.attendance.mapper.AttendanceMapper;
import com.cts.rivio.modules.attendance.repository.AttendanceRepository;
import com.cts.rivio.modules.attendance.repository.HolidayRepository;
import com.cts.rivio.modules.attendance.repository.WorkDayRepository;
import com.cts.rivio.modules.attendance.service.AttendanceService;
import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.employee.repository.EmployeeProfileRepository;
import com.cts.rivio.modules.leave.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeProfileRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;
    private final LeaveRequestRepository leaveRequestRepository;
    private final WorkDayRepository workDayRepository;
    private final HolidayRepository holidayRepository;

    @Override
    @Transactional
    public AttendanceResponse markAttendance(AttendanceRequest request, Integer createdByUserId) {
        // Application-level safety check
        if (attendanceRepository.existsByEmployeeProfileIdAndDate(request.getEmployeeProfileId(), request.getDate())) {
            throw new IllegalArgumentException("Attendance already marked for this employee on this date.");
        }

        EmployeeProfile employee = employeeRepository.findById(request.getEmployeeProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", request.getEmployeeProfileId()));

        // FIX (AC 2): Auto-resolve the status dynamically
        AttendanceStatus resolvedStatus = autoResolveStatus(request.getEmployeeProfileId(),request.getDate(),request.getPunchIn(), request.getPunchOut());

        Attendance attendance = Attendance.builder()
                .employeeProfile(employee)
                .date(request.getDate())
                .punchIn(request.getPunchIn())
                .punchOut(request.getPunchOut())
                .status(resolvedStatus)
                .build();

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    /**
     * [ATTN-33] Master logic to resolve attendance status hierarchically.
     */
    private AttendanceStatus autoResolveStatus(Integer employeeId, LocalDate date, LocalDateTime punchIn, LocalDateTime punchOut) {

        // AC 1: If approved leave exists -> Status = LEAVE
        if (leaveRequestRepository.hasApprovedLeaveOnDate(employeeId, date)) {
            return AttendanceStatus.LEAVE;
        }

        // AC 2: If Holiday exists -> Status = HOLIDAY
        if (holidayRepository.existsByDate(date)) {
            return AttendanceStatus.HOLIDAY;
        }

        // Cross-check Working Days table (If it's a weekend/non-working day -> Status = HOLIDAY)
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        WorkDay workDayConfig = workDayRepository.findByDayName(dayName).orElse(null);

        if (workDayConfig != null && !workDayConfig.getIsWorkingDay()) {
            return AttendanceStatus.HOLIDAY;
        }

        // AC 3: If valid punches -> Status = PRESENT
        if (punchIn == null && punchOut == null) {
            return AttendanceStatus.ABSENT;
        }

        if (punchIn != null && punchOut != null) {
            long hoursWorked = java.time.Duration.between(punchIn, punchOut).toHours();
            if (hoursWorked < 4) {
                return AttendanceStatus.ABSENT; // Less than 4 hours is absent
            } else {
                return AttendanceStatus.PRESENT; // 4+ hours is marked as Present
            }
        }

        // Still clocked in
        return AttendanceStatus.PRESENT;
    }

    @Override
    @Transactional
    public AttendanceResponse updatePunchOut(Long id, PunchOutRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance Record", "id", id));

        // Set the new punch out time
        attendance.setPunchOut(request.getPunchOut());

        // FIX: Recalculate the status now that we have the final punchOut time
        AttendanceStatus finalStatus = autoResolveStatus(
                attendance.getEmployeeProfile().getId(),
                attendance.getDate(),
                attendance.getPunchIn(),
                request.getPunchOut()
        );
        attendance.setStatus(finalStatus);

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    public List<AttendanceResponse> getOrganizationAttendance(LocalDate date) {
        List<Attendance> records = (date != null) ? attendanceRepository.findByDate(date) : attendanceRepository.findAll();
        return records.stream().map(attendanceMapper::toResponse).collect(Collectors.toList());
    }
}