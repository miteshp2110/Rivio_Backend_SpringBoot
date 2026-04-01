package com.cts.rivio.modules.ats.service;

import com.cts.rivio.modules.ats.dto.request.CandidateRequest;
import com.cts.rivio.modules.ats.dto.request.JobOpeningRequest;
import com.cts.rivio.modules.ats.dto.request.JobStatusUpdateRequest;
import com.cts.rivio.modules.ats.entity.Candidate;
import com.cts.rivio.modules.ats.entity.JobOpening;
import com.cts.rivio.modules.ats.enums.CandidateStage;
import java.util.List;

public interface JobOpeningService {
    JobOpening createJobOpening(JobOpeningRequest request);
    List<JobOpening> getAllJobOpenings();
    JobOpening updateJobStatus(Long id, JobStatusUpdateRequest request);
    Candidate addCandidate(Long jobOpeningId, CandidateRequest request);

    // [ATS-37] New method to view candidates
    List<Candidate> getCandidatesByJobId(Long jobOpeningId, CandidateStage stage);
}