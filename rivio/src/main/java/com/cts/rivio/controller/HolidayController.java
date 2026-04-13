package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.HolidayRequest;
import com.cts.rivio.dto.response.HolidayResponse;
import com.cts.rivio.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping
    public ResponseEntity<ApiResponse<HolidayResponse>> addHoliday(@Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = holidayService.createHoliday(request);
        // Standardized wrapper
        return new ResponseEntity<>(ApiResponse.success(response, "Holiday created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HolidayResponse>>> getAllHolidays() {
        List<HolidayResponse> holidays = holidayService.getAllHolidays();
        // Standardized wrapper
        return ResponseEntity.ok(ApiResponse.success(holidays, "Holidays fetched successfully"));
    }

    // --- NEW ENDPOINT ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Integer id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Holiday deleted successfully"));
    }
}