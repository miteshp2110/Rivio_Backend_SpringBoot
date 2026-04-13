package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.service.PayCycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pay-cycles")
@RequiredArgsConstructor
public class PayCycleController {

    private final PayCycleService payCycleService;

    /**
     * [PAY-42] Create a new Pay Cycle
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PayCycleResponse>> createPayCycle(@Valid @RequestBody PayCycleRequest request) {
        PayCycleResponse response = payCycleService.createPayCycle(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Pay Cycle created successfully in Draft mode"), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PayCycleResponse>>> getAllPayCycles(
            @RequestParam(required = false) String name) {

        List<PayCycleResponse> responses = payCycleService.getAllPayCycles(name);
        return ResponseEntity.ok(ApiResponse.success(responses, "Pay cycles fetched successfully"));
    }
}