package com.cts.rivio.modules.ats.service;

import com.cts.rivio.modules.ats.dto.request.StageUpdateRequest;
import com.cts.rivio.modules.ats.entity.Candidate;
import java.util.Map;

public interface CandidateService {
    // [ATS-38] Move Stages
    Candidate updateCandidateStage(Long id, StageUpdateRequest request);

    // [ATS-39] Hire Candidate & Trigger Profile
    Map<String, Object> hireCandidate(Long candidateId);
}