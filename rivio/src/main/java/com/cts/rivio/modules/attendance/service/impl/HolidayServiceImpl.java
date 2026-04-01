package com.cts.rivio.modules.attendance.service.impl; // Ensure this matches your folder

import com.cts.rivio.modules.attendance.dto.HolidayRequest;
import com.cts.rivio.modules.attendance.dto.HolidayResponse;
import com.cts.rivio.modules.attendance.entity.Holiday;
import com.cts.rivio.modules.attendance.repository.HolidayRepository;
import com.cts.rivio.modules.attendance.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List; // ADD THIS
import java.util.stream.Collectors; // ADD THIS

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public HolidayResponse createHoliday(HolidayRequest request) {
        // ... (Your existing code for creating a holiday remains here) ...
        if (holidayRepository.existsByDate(request.getDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Date already exists!");
        }

        Holiday holiday = Holiday.builder()
                .date(request.getDate())
                .name(request.getName())
                .build();

        Holiday saved = holidayRepository.save(holiday);

        HolidayResponse response = new HolidayResponse();
        response.setId(saved.getId());
        response.setDate(saved.getDate());
        response.setName(saved.getName());

        return response;
    }

    // ADD THIS NEW METHOD HERE
    @Override
    public List<HolidayResponse> getAllHolidays() {
        // 1. Fetch all records from the database table
        List<Holiday> holidays = holidayRepository.findAll();

        // 2. Convert each 'Holiday' entity into a 'HolidayResponse' DTO
        return holidays.stream().map(holiday -> {
            HolidayResponse res = new HolidayResponse();
            res.setId(holiday.getId());
            res.setDate(holiday.getDate());
            res.setName(holiday.getName());
            return res;
        }).collect(Collectors.toList());
    }
}