package com.cts.rivio.modules.attendance.service;

import com.cts.rivio.modules.attendance.dto.request.WorkDayRequest;
import com.cts.rivio.modules.attendance.dto.response.WorkDayResponse;
import java.util.List;

public interface WorkDayService {
    List<WorkDayResponse> getAllWorkDays();
    WorkDayResponse updateWorkDay(Integer id, WorkDayRequest request);
}