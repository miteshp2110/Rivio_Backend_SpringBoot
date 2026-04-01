package com.cts.rivio.modules.company.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.company.dto.request.LocationRequest;
import com.cts.rivio.modules.company.dto.response.LocationResponse;
import com.cts.rivio.modules.company.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<ApiResponse<LocationResponse>> createLocation(@RequestBody LocationRequest request) {
        LocationResponse response = locationService.createLocation(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Location created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAllLocations() {
        List<LocationResponse> response = locationService.getAllLocations();
        return ResponseEntity.ok(ApiResponse.success(response, "Locations fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> updateLocation(@PathVariable Integer id, @RequestBody LocationRequest request) {
        LocationResponse response = locationService.updateLocation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Location updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Location deleted successfully"));
    }
}