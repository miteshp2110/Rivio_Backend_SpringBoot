package com.cts.rivio.modules.attendance.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.attendance.dto.request.WorkDayRequest;
import com.cts.rivio.modules.attendance.dto.response.WorkDayResponse;
import com.cts.rivio.modules.attendance.service.WorkDayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-days")
@RequiredArgsConstructor
public class WorkDayController {

    private final WorkDayService workDayService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkDayResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(workDayService.getAllWorkDays(), "Work days fetched"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkDayResponse>> updateWorkDay(@PathVariable Integer id, @Valid @RequestBody WorkDayRequest request) {
        return ResponseEntity.ok(ApiResponse.success(workDayService.updateWorkDay(id, request), "Work day updated"));
    }
}