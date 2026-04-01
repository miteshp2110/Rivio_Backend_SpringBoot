package com.cts.rivio.modules.attendance.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.attendance.dto.request.HolidayRequest;
import com.cts.rivio.modules.attendance.dto.response.HolidayResponse;
import com.cts.rivio.modules.attendance.entity.Holiday;
import com.cts.rivio.modules.attendance.repository.HolidayRepository;
import com.cts.rivio.modules.attendance.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public HolidayResponse createHoliday(HolidayRequest request) {
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

    @Override
    public List<HolidayResponse> getAllHolidays() {
        // AC 1: Fetch all records chronologically
        List<Holiday> holidays = holidayRepository.findAllByOrderByDateAsc();

        return holidays.stream().map(holiday -> {
            HolidayResponse res = new HolidayResponse();
            res.setId(holiday.getId());
            res.setDate(holiday.getDate());
            res.setName(holiday.getName());
            return res;
        }).collect(Collectors.toList());
    }

    // --- NEW METHOD (AC 2) ---
    @Override
    public void deleteHoliday(Integer id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday", "id", id));

        holidayRepository.delete(holiday);
    }
}