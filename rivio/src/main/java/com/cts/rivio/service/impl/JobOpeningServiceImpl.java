package com.cts.rivio.service.impl;

import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.dto.request.JobStatusUpdateRequest;
import com.cts.rivio.entity.Candidate;
import com.cts.rivio.entity.JobOpening;
import com.cts.rivio.enums.CandidateStage;
import com.cts.rivio.enums.JobStatus;
import com.cts.rivio.service.JobOpeningService;
import com.cts.rivio.entity.Department;
import com.cts.rivio.entity.Location;
import com.cts.rivio.repository.DepartmentRepository;
import com.cts.rivio.repository.LocationRepository;
import com.cts.rivio.repository.CandidateRepository;
import com.cts.rivio.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOpeningServiceImpl implements JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CandidateRepository candidateRepository;
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public JobOpening createJobOpening(JobOpeningRequest request) {
        // Find Dept/Loc using Integer IDs from your schema
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found ID: " + request.getDepartmentId()));

        Location loc = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found ID: " + request.getLocationId()));

        JobOpening opening = JobOpening.builder()
                .title(request.getTitle())
                .department(dept)
                .location(loc)
                .status(JobStatus.OPEN) // AC: Default status is 'Open'
                .build();
        return jobOpeningRepository.save(opening);
    }

    @Override
    @Transactional
    public Candidate addCandidate(Long jobOpeningId, CandidateRequest request) {
        JobOpening opening = jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new RuntimeException("Job Opening not found"));

        // AC: Email unique per job opening
        if (candidateRepository.existsByEmailAndJobOpeningId(request.getEmail(), jobOpeningId)) {
            throw new RuntimeException("Candidate with email " + request.getEmail() + " already applied.");
        }

        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .email(request.getEmail())
                .resumeUrl(request.getResumeUrl())
                .jobOpening(opening)
                .stage(CandidateStage.APPLIED) // AC: Default is 'Applied'
                .build();
        return candidateRepository.save(candidate);
    }

    @Override
    public List<JobOpening> getAllJobOpenings() {
        return jobOpeningRepository.findAll();
    }

    @Override
    public JobOpening updateJobStatus(Long id, JobStatusUpdateRequest request) {
        JobOpening opening = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        opening.setStatus(request.getStatus());
        return jobOpeningRepository.save(opening);
    }

    @Override
    public List<Candidate> getCandidatesByJobId(Long jobOpeningId, CandidateStage stage) {
        if (stage != null) {
            return candidateRepository.findByJobOpeningIdAndStage(jobOpeningId, stage);
        }
        return candidateRepository.findByJobOpeningId(jobOpeningId);
    }
}