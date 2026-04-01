package com.cts.rivio.modules.attendance.service;

import com.cts.rivio.modules.attendance.dto.request.AttendanceRequest;
import com.cts.rivio.modules.attendance.dto.request.PunchOutRequest;
import com.cts.rivio.modules.attendance.dto.response.AttendanceResponse;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(AttendanceRequest request, Integer createdByUserId);
    AttendanceResponse updatePunchOut(Long id, PunchOutRequest request);
    List<AttendanceResponse> getOrganizationAttendance(LocalDate date);
}