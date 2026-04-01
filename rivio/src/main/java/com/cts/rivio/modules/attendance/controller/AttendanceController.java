package com.cts.rivio.modules.attendance.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.attendance.dto.request.AttendanceRequest;
import com.cts.rivio.modules.attendance.dto.request.PunchOutRequest;
import com.cts.rivio.modules.attendance.dto.response.AttendanceResponse;
import com.cts.rivio.modules.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getOrganizationAttendance(date), "Attendance records fetched"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
            @Valid @RequestBody AttendanceRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Integer createdByUserId) {
        AttendanceResponse response = attendanceService.markAttendance(request, createdByUserId);
        return new ResponseEntity<>(ApiResponse.success(response, "Attendance marked successfully"), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/punch-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updatePunchOut(
            @PathVariable Long id,
            @Valid @RequestBody PunchOutRequest request) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.updatePunchOut(id, request), "Punch out updated successfully"));
    }

    // [ATTN-29] Bulk Upload (Mocked for now as Excel parsing requires Apache POI)
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadAttendance(@RequestParam("file") MultipartFile file) {
        // Logic to parse CSV/Excel goes here
        return ResponseEntity.ok(ApiResponse.success(null, "File uploaded and processed successfully"));
    }
}