package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.StageUpdateRequest;
import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.entity.Candidate;
import com.cts.rivio.enums.CandidateStage;
import com.cts.rivio.mapper.CandidateMapper;
import com.cts.rivio.repository.CandidateRepository;
import com.cts.rivio.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    @Override
    public Candidate updateCandidateStage(Long id, StageUpdateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidate.setStage(request.getStage());
        return candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public Map<String, Object> hireCandidate(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // AC: 1. Update status to HIRED
        candidate.setStage(CandidateStage.HIRED);
        candidateRepository.save(candidate);

        // AC: 2. Trigger creation of Employee Profile (Simulated via Response)
        String empCode = "RIV-EMP-" + (3000 + candidate.getId());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Candidate marked as Hired. Profile Draft Created.");
        response.put("generatedEmployeeCode", empCode);
        response.put("email", candidate.getEmail());

        return response;
    }

    @Override
    public CandidateDTO getCandidateById(Integer id) {
        Candidate candidate = candidateRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", id));

        return candidateMapper.toDto(candidate);
    }


}