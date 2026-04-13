package com.cts.rivio.service;

import com.cts.rivio.dto.request.WorkDayRequest;
import com.cts.rivio.dto.response.WorkDayResponse;
import java.util.List;

public interface WorkDayService {
    List<WorkDayResponse> getAllWorkDays();
    WorkDayResponse updateWorkDay(Integer id, WorkDayRequest request);
}