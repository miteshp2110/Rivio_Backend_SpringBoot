package com.cts.rivio.modules.attendance.controller;

import com.cts.rivio.modules.attendance.dto.HolidayRequest;
import com.cts.rivio.modules.attendance.dto.HolidayResponse;
import com.cts.rivio.modules.attendance.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // ADD THIS IMPORT

@RestController
@RequestMapping("/holidays") // Use /api/holidays to stay consistent with Rivio standards
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    // --- POST Method (Create) ---
    @PostMapping
    public ResponseEntity<HolidayResponse> addHoliday(@Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = holidayService.createHoliday(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- GET Method (Retrieve All) ---
    @GetMapping // ADD THIS METHOD
    public ResponseEntity<List<HolidayResponse>> getAllHolidays() {
        // 1. Call the service method we just added to the implementation
        List<HolidayResponse> holidays = holidayService.getAllHolidays();

        // 2. Return the list with a 200 OK status
        return ResponseEntity.ok(holidays);
    }
}