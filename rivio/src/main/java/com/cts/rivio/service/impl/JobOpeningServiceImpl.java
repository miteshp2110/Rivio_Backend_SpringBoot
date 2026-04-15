package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.CandidateRequest;
import com.cts.rivio.dto.request.JobOpeningRequest;
import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.dto.response.JobOpeningResponse;
import com.cts.rivio.entity.Candidate;
import com.cts.rivio.entity.Department;
import com.cts.rivio.entity.JobOpening;
import com.cts.rivio.entity.Location;
import com.cts.rivio.enums.CandidateStage;
import com.cts.rivio.enums.JobStatus;
import com.cts.rivio.mapper.CandidateMapper;
import com.cts.rivio.mapper.JobOpeningMapper;
import com.cts.rivio.repository.CandidateRepository;
import com.cts.rivio.repository.DepartmentRepository;
import com.cts.rivio.repository.JobOpeningRepository;
import com.cts.rivio.repository.LocationRepository;
import com.cts.rivio.service.JobOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOpeningServiceImpl implements JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CandidateRepository candidateRepository;
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;

    // FIX 1: Properly injected the Mappers!
    private final JobOpeningMapper jobOpeningMapper;
    private final CandidateMapper candidateMapper;

    @Override
    @Transactional
    public JobOpeningResponse createJobOpening(JobOpeningRequest request) {
        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Location loc = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", request.getLocationId()));

        JobOpening opening = JobOpening.builder()
                .title(request.getTitle())
                .department(dept)
                .location(loc)
                .status(JobStatus.OPEN) // AC: Default status is 'Open'
                .build();

        // FIX 2: Return the mapped DTO, not the Entity
        return jobOpeningMapper.toResponse(jobOpeningRepository.save(opening));
    }

    @Override
    public List<JobOpeningResponse> getAllJobOpenings() {
        return jobOpeningRepository.findAll()
                .stream()
                .map(jobOpeningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobOpeningResponse updateStatus(Integer id, JobStatus newStatus) {
        JobOpening jobOpening = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Opening", "id", id));

        if (jobOpening.getStatus() == newStatus) {
            return jobOpeningMapper.toResponse(jobOpening);
        }

        jobOpening.setStatus(newStatus);
        return jobOpeningMapper.toResponse(jobOpeningRepository.save(jobOpening));
    }

    @Override
    @Transactional
    public void deleteJobOpening(Integer id) {
        JobOpening jobOpening = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Opening", "id", id));

        // AC 1: Safe Delete Check
        if (candidateRepository.existsByJobOpeningId(id)) {
            throw new IllegalStateException("Cannot delete Job Opening: Candidates have already applied. Please change the status to 'CLOSED' instead.");
        }

        jobOpeningRepository.delete(jobOpening);
    }

    // --- CANDIDATE METHODS --- //

    @Override
    @Transactional
    public CandidateDTO addCandidate(Integer jobOpeningId, CandidateRequest request) {
        JobOpening opening = jobOpeningRepository.findById(jobOpeningId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Opening", "id", jobOpeningId));

        if (candidateRepository.existsByEmailAndJobOpeningId(request.getEmail(),  jobOpeningId)) {
            throw new IllegalArgumentException("Candidate with email " + request.getEmail() + " already applied.");
        }

        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .email(request.getEmail())
                .resumeUrl(request.getResumeUrl())
                .jobOpening(opening)
                .stage(CandidateStage.APPLIED) // AC: Default is 'Applied'
                .build();

        return candidateMapper.toDto(candidateRepository.save(candidate));
    }

    @Override
    public List<CandidateDTO> getCandidatesByJobId(Integer jobOpeningId, CandidateStage stage) { // FIX 3: Changed Long to Integer
        List<Candidate> candidates;

        if (stage != null) {
            candidates = candidateRepository.findByJobOpeningIdAndStage(jobOpeningId, stage);
        } else {
            candidates = candidateRepository.findByJobOpeningId(jobOpeningId);
        }

        return candidates.stream()
                .map(candidateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobOpeningResponse getJobOpeningById(Integer id) {
        JobOpening jobOpening = jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Opening", "id", id));

        return jobOpeningMapper.toResponse(jobOpening);
    }
}