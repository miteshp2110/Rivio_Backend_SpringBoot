package com.cts.rivio.service;

import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.dto.response.JobOpeningResponse;
import com.cts.rivio.enums.CandidateStage;
import com.cts.rivio.enums.JobStatus;

import java.util.List;

public interface JobOpeningService {
    JobOpeningResponse createJobOpening(JobOpeningRequest request);
    List<JobOpeningResponse> getAllJobOpenings();
    JobOpeningResponse updateStatus(Integer id, JobStatus newStatus);
    void deleteJobOpening(Integer id);

    CandidateDTO addCandidate(Integer jobOpeningId, CandidateRequest request);
    List<CandidateDTO> getCandidatesByJobId(Integer jobOpeningId, CandidateStage stage);
    JobOpeningResponse getJobOpeningById(Integer id);
}