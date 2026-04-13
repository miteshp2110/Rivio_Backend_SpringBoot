package com.cts.rivio.service;

import com.cts.rivio.dto.request.AttendanceRequest;
import com.cts.rivio.dto.request.PunchOutRequest;
import com.cts.rivio.dto.response.AttendanceResponse;
import com.cts.rivio.dto.response.BulkUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(AttendanceRequest request, Integer createdByUserId);
    AttendanceResponse updatePunchOut(Long id, PunchOutRequest request);
    List<AttendanceResponse> getOrganizationAttendance(LocalDate date);
    BulkUploadResponse processBulkUpload(MultipartFile file);
}