package com.cts.rivio.modules.attendance.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.attendance.dto.request.AttendanceRequest;
import com.cts.rivio.modules.attendance.dto.request.PunchOutRequest;
import com.cts.rivio.modules.attendance.dto.response.AttendanceResponse;
import com.cts.rivio.modules.attendance.entity.Attendance;
import com.cts.rivio.modules.attendance.enums.AttendanceStatus;
import com.cts.rivio.modules.attendance.mapper.AttendanceMapper;
import com.cts.rivio.modules.attendance.repository.AttendanceRepository;
import com.cts.rivio.modules.attendance.service.AttendanceService;
import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.employee.repository.EmployeeProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeProfileRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

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
        AttendanceStatus resolvedStatus = autoResolveStatus(request.getPunchIn(), request.getPunchOut());

        Attendance attendance = Attendance.builder()
                .employeeProfile(employee)
                .date(request.getDate())
                .punchIn(request.getPunchIn())
                .punchOut(request.getPunchOut())
                .status(resolvedStatus)
                .build();

        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    // --- NEW HELPER METHOD ---
    private AttendanceStatus autoResolveStatus(LocalDateTime punchIn, LocalDateTime punchOut) {
        if (punchIn == null && punchOut == null) {
            return AttendanceStatus.ABSENT;
        }

        if (punchIn != null && punchOut != null) {
            // Calculate hours worked
            long hoursWorked = java.time.Duration.between(punchIn, punchOut).toHours();

            if (hoursWorked < 4) {
                return AttendanceStatus.ABSENT; // Less than 4 hours is considered absent
            } else if (hoursWorked < 8) {
                // If your enum has Half_Day, use it here. Otherwise, default to your business rules.
                // return AttendanceStatus.Half_Day;
                return AttendanceStatus.PRESENT;
            } else {
                return AttendanceStatus.PRESENT;
            }
        }

        // If they punched in but haven't punched out yet
        return AttendanceStatus.PRESENT;
    }

    @Override
    @Transactional
    public AttendanceResponse updatePunchOut(Long id, PunchOutRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance Record", "id", id));

        attendance.setPunchOut(request.getPunchOut());
        return attendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    public List<AttendanceResponse> getOrganizationAttendance(LocalDate date) {
        List<Attendance> records = (date != null) ? attendanceRepository.findByDate(date) : attendanceRepository.findAll();
        return records.stream().map(attendanceMapper::toResponse).collect(Collectors.toList());
    }
}