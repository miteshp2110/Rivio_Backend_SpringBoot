package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.response.DashboardResponse;
import com.cts.rivio.enums.EmployeeStatus;
import com.cts.rivio.enums.JobStatus;
import com.cts.rivio.enums.LeaveStatus;
import com.cts.rivio.repository.CandidateRepository;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.repository.JobOpeningRepository;
import com.cts.rivio.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final EmployeeProfileRepository employeeRepository;
    private final JobOpeningRepository jobOpeningRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final CandidateRepository candidateRepository;

    @GetMapping("/admin-summary")
    public ResponseEntity<ApiResponse<DashboardResponse>> getAdminSummary() {

        // Count queries are highly optimized in SQL (much faster than fetching lists)
        long activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE).size();
        long openJobs = jobOpeningRepository.findAll().stream().filter(j -> j.getStatus() == JobStatus.OPEN).count();
        long pendingLeaves = leaveRequestRepository.findAll().stream().filter(l -> l.getStatus() == LeaveStatus.PENDING).count();
        long totalCandidates = candidateRepository.count();

        DashboardResponse summary = DashboardResponse.builder()
                .totalActiveEmployees(activeEmployees)
                .totalOpenJobs(openJobs)
                .pendingLeaveRequests(pendingLeaves)
                .totalCandidates(totalCandidates)
                .build();

        return ResponseEntity.ok(ApiResponse.success(summary, "Dashboard metrics fetched"));
    }
}