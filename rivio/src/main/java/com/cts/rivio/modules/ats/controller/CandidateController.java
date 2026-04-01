package com.cts.rivio.modules.ats.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.ats.dto.request.StageUpdateRequest;
import com.cts.rivio.modules.ats.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PutMapping("/{id}/stage")
    public ResponseEntity<ApiResponse<?>> updateStage(@PathVariable Long id, @RequestBody StageUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(candidateService.updateCandidateStage(id, request), "Stage updated"));
    }

    @PostMapping("/{id}/hire")
    public ResponseEntity<ApiResponse<Map<String, Object>>> hire(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(candidateService.hireCandidate(id), "Hiring process completed"));
    }
}