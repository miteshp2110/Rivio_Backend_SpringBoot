package com.cts.rivio.service;

import com.cts.rivio.dto.request.PayCycleRequest;
import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.enums.PayCycleStatus;

import java.util.List;

public interface PayCycleService {
    PayCycleResponse createPayCycle(PayCycleRequest request);
    List<PayCycleResponse> getAllPayCycles(String searchName);
    PayCycleResponse updateStatus(Integer id, PayCycleStatus newStatus);


}