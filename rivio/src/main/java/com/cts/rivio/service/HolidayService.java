package com.cts.rivio.service;

import com.cts.rivio.dto.request.HolidayRequest;
import com.cts.rivio.dto.response.HolidayResponse;
import java.util.List;

public interface HolidayService {
    HolidayResponse createHoliday(HolidayRequest request);
    List<HolidayResponse> getAllHolidays();

    // --- NEW METHOD ---
    void deleteHoliday(Integer id);
}