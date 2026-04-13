package com.cts.rivio.service;

import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.response.PayCycleResponse;

import java.util.List;

public interface PayCycleService {
    PayCycleResponse createPayCycle(PayCycleRequest request);
    List<PayCycleResponse> getAllPayCycles(String searchName);
}