package com.cts.rivio.service;

import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.dto.request.JobStatusUpdateRequest;
import com.cts.rivio.entity.Candidate;
import com.cts.rivio.entity.JobOpening;
import com.cts.rivio.enums.CandidateStage;
import java.util.List;

public interface JobOpeningService {
    JobOpening createJobOpening(JobOpeningRequest request);
    List<JobOpening> getAllJobOpenings();
    JobOpening updateJobStatus(Long id, JobStatusUpdateRequest request);
    Candidate addCandidate(Long jobOpeningId, CandidateRequest request);

    // [ATS-37] New method to view candidates
    List<Candidate> getCandidatesByJobId(Long jobOpeningId, CandidateStage stage);
}