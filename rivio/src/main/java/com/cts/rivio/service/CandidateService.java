package com.cts.rivio.service;

import com.cts.rivio.dto.request.StageUpdateRequest;
import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.entity.Candidate;
import java.util.Map;

public interface CandidateService {

    CandidateDTO getCandidateById(Integer id);
    // [ATS-38] Move Stages
    Candidate updateCandidateStage(Long id, StageUpdateRequest request);

    // [ATS-39] Hire Candidate & Trigger Profile
    Map<String, Object> hireCandidate(Long candidateId);
}