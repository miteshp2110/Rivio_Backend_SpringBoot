package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.dto.request.JobStatusUpdateRequest;
import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.dto.response.JobOpeningResponse;
import com.cts.rivio.enums.CandidateStage;
import com.cts.rivio.service.JobOpeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/job-openings")
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    // ==========================================
    // JOB OPENING ENDPOINTS
    // ==========================================

    @PostMapping
    public ResponseEntity<ApiResponse<JobOpeningResponse>> createJobOpening(@Valid @RequestBody JobOpeningRequest request) {
        JobOpeningResponse response = jobOpeningService.createJobOpening(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Job opening created successfully in OPEN status"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobOpeningResponse>>> getAllJobOpenings() {
        List<JobOpeningResponse> responses = jobOpeningService.getAllJobOpenings();
        return ResponseEntity.ok(ApiResponse.success(responses, "Job openings fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobOpeningResponse>> getJobOpeningById(@PathVariable Integer id) {
        JobOpeningResponse response = jobOpeningService.getJobOpeningById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Job opening fetched successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobOpeningResponse>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody JobStatusUpdateRequest request) {
        JobOpeningResponse response = jobOpeningService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(response, "Job Opening status updated to " + request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJobOpening(@PathVariable Integer id) {
        jobOpeningService.deleteJobOpening(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Job Opening successfully deleted."));
    }

    // ==========================================
    // CANDIDATE ENDPOINTS (Nested under Job Openings)
    // ==========================================

    /**
     * Add a candidate to a specific job opening
     */
    @PostMapping("/{id}/candidates")
    public ResponseEntity<ApiResponse<CandidateDTO>> addCandidate(
            @PathVariable Integer id,
            @Valid @RequestBody CandidateRequest request) {
        CandidateDTO savedCandidate = jobOpeningService.addCandidate(id, request);
        return new ResponseEntity<>(ApiResponse.success(savedCandidate, "Candidate added successfully"), HttpStatus.CREATED);
    }

    /**
     * Get all candidates for a specific job opening (with optional stage filter)
     */
    @GetMapping("/{id}/candidates")
    public ResponseEntity<ApiResponse<List<CandidateDTO>>> getCandidates(
            @PathVariable Integer id,
            @RequestParam(required = false) CandidateStage stage) {
        List<CandidateDTO> candidates = jobOpeningService.getCandidatesByJobId(id, stage);
        return ResponseEntity.ok(ApiResponse.success(candidates, "Candidates retrieved successfully"));
    }
}