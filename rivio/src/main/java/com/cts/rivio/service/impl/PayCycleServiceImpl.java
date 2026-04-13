package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.entity.PayCycle;
import com.cts.rivio.enums.PayCycleStatus;
import com.cts.rivio.mapper.PayCycleMapper;
import com.cts.rivio.repository.PayCycleRepository;
import com.cts.rivio.service.PayCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayCycleServiceImpl implements PayCycleService {

    private final PayCycleRepository payCycleRepository;
    private final PayCycleMapper payCycleMapper;

    @Override
    public PayCycleResponse createPayCycle(PayCycleRequest request) {
        // Date sanity check
        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new IllegalArgumentException("From date cannot be after To date.");
        }

        // AC 1: Dates cannot overlap with existing Finalized cycles
        // Note: Ensure 'FINALIZED' matches the exact name in your PayCycleStatus.java enum!
        boolean isOverlapping = payCycleRepository.hasOverlappingCycle(
                PayCycleStatus.FINALIZED,
                request.getFromDate(),
                request.getToDate()
        );

        if (isOverlapping) {
            throw new IllegalArgumentException("Cannot create pay cycle: The dates overlap with an existing finalized cycle.");
        }

        // AC 2: Default status is 'Draft'
        PayCycle payCycle = PayCycle.builder()
                .cycleName(request.getCycleName())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .status(PayCycleStatus.DRAFT)
                .build();

        return payCycleMapper.toResponse(payCycleRepository.save(payCycle));
    }

    @Override
    public List<PayCycleResponse> getAllPayCycles(String searchName) {
        List<PayCycle> payCycles;

        // If user typed a name, search for it. Otherwise, get all.
        if (searchName != null && !searchName.trim().isEmpty()) {
            payCycles = payCycleRepository.findByCycleNameContainingIgnoreCase(searchName.trim());
        } else {
            payCycles = payCycleRepository.findAll();
        }

        // Convert the Entities to DTOs
        return payCycles.stream()
                .map(payCycleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PayCycleResponse updateStatus(Integer id, PayCycleStatus newStatus) {
        PayCycle cycle = payCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PayCycle", "id", id));

        PayCycleStatus currentStatus = cycle.getStatus();

        // If they send the exact same status, do nothing and return
        if (currentStatus == newStatus) {
            return payCycleMapper.toResponse(cycle);
        }

        // AC 2: Once 'Paid', cannot revert to 'Draft' (or anything else)
        if (currentStatus == PayCycleStatus.PAID) {
            throw new IllegalArgumentException("Cannot change status. This Pay Cycle has already been PAID.");
        }

        // State Machine validation
        boolean isValidTransition = false;

        switch (currentStatus) {
            case DRAFT:
                // From Draft, you can only move forward to Processing
                isValidTransition = (newStatus == PayCycleStatus.PROCESSING);
                break;

            case PROCESSING:
                // From Processing, move forward to Finalized, or revert to Draft to fix errors
                isValidTransition = (newStatus == PayCycleStatus.FINALIZED || newStatus == PayCycleStatus.DRAFT);
                break;

            case FINALIZED:
                // From Finalized, move forward to Paid, or revert to Draft to fix major errors
                isValidTransition = (newStatus == PayCycleStatus.PAID || newStatus == PayCycleStatus.DRAFT);
                break;
        }

        // AC 1: Must follow sequential order
        if (!isValidTransition) {
            throw new IllegalArgumentException(
                    String.format("Invalid transition. Cannot move from %s directly to %s.", currentStatus, newStatus)
            );
        }

        // If transitioning back to DRAFT, we should clear the generated payslips!
        if (newStatus == PayCycleStatus.DRAFT) {
            // Optionally: Inject PaySlipRepository here and call paySlipRepository.deleteByPayCycleId(cycle.getId());
            // This ensures if they revert to Draft, the old processed payslips are wiped clean.
        }

        cycle.setStatus(newStatus);
        return payCycleMapper.toResponse(payCycleRepository.save(cycle));
    }
}