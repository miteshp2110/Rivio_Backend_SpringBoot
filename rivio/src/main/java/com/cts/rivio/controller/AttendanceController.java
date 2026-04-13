package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.AttendanceRequest;
import com.cts.rivio.dto.request.PunchOutRequest;
import com.cts.rivio.dto.response.AttendanceResponse;
import com.cts.rivio.dto.response.BulkUploadResponse;
import com.cts.rivio.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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



    @GetMapping("/upload/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        String csvHeader = "employeeId,date,punchIn,punchOut\n";
        String sampleData = "1,2026-04-13,2026-04-13T09:00,2026-04-13T17:00\n"; // Example row
        byte[] csvBytes = (csvHeader + sampleData).getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "attendance_upload_template.csv");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BulkUploadResponse>> uploadAttendance(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Please upload a valid CSV file."));
        }

        BulkUploadResponse result = attendanceService.processBulkUpload(file);

        String msg = result.getFailedRecords() > 0
                ? "Upload completed with some errors. Please check the logs."
                : "All records uploaded successfully!";

        return ResponseEntity.ok(ApiResponse.success(result, msg));
    }
}