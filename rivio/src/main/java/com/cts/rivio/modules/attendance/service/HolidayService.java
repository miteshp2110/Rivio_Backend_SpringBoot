package com.cts.rivio.modules.attendance.service;

import com.cts.rivio.modules.attendance.dto.HolidayRequest;
import com.cts.rivio.modules.attendance.dto.HolidayResponse;
import java.util.List; // 1. Make sure this import is at the top

public interface HolidayService {

    // Method for POST (Creating)
    HolidayResponse createHoliday(HolidayRequest request);

    // Method for GET (Retrieving all)
    List<HolidayResponse> getAllHolidays();
}