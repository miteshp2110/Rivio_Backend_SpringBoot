package com.cts.rivio.service.impl;

import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.entity.PayCycle;
import com.cts.rivio.enums.PayCycleStatus;
import com.cts.rivio.mapper.PayCycleMapper;
import com.cts.rivio.repository.PayCycleRepository;
import com.cts.rivio.service.PayCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}