package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.entity.Candidate;
import com.cts.rivio.entity.JobOpening;
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

    @PostMapping
    public ResponseEntity<ApiResponse<JobOpening>> create(@Valid @RequestBody JobOpeningRequest request) {
        JobOpening saved = jobOpeningService.createJobOpening(request);
        return new ResponseEntity<>(ApiResponse.success(saved, "Opening created successfully"), HttpStatus.CREATED);
    }

    // POST /api/job-openings/{id}/candidates -> To ADD a new candidate
    @PostMapping("/{id}/candidates")
    public ResponseEntity<ApiResponse<Candidate>> addCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateRequest request) {
        Candidate savedCandidate = jobOpeningService.addCandidate(id, request);
        return new ResponseEntity<>(ApiResponse.success(savedCandidate, "Candidate added successfully"), HttpStatus.CREATED);
    }

    // GET /api/job-openings/{id}/candidates -> To VIEW candidates (ATS-37)
    @GetMapping("/{id}/candidates")
    public ResponseEntity<ApiResponse<List<Candidate>>> getCandidates(
            @PathVariable Long id,
            @RequestParam(required = false) CandidateStage stage) {
        List<Candidate> candidates = jobOpeningService.getCandidatesByJobId(id, stage);
        return ResponseEntity.ok(ApiResponse.success(candidates, "Candidates retrieved successfully"));
    }
}