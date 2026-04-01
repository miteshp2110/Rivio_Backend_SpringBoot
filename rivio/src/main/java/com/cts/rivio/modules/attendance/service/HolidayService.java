package com.cts.rivio.modules.attendance.service;

import com.cts.rivio.modules.attendance.dto.request.HolidayRequest;
import com.cts.rivio.modules.attendance.dto.response.HolidayResponse;
import java.util.List;

public interface HolidayService {
    HolidayResponse createHoliday(HolidayRequest request);
    List<HolidayResponse> getAllHolidays();

    // --- NEW METHOD ---
    void deleteHoliday(Integer id);
}