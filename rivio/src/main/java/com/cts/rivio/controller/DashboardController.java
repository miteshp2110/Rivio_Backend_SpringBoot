package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.response.DashboardResponse;
import com.cts.rivio.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin-summary")
    public ResponseEntity<ApiResponse<DashboardResponse>> getAdminSummary() {
        DashboardResponse summary = dashboardService.getAdminSummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Dashboard metrics fetched successfully"));
    }
}