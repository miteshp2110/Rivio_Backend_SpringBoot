package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.LocationRequest;
import com.cts.rivio.dto.response.LocationResponse;
import com.cts.rivio.entity.Location;
import com.cts.rivio.mapper.LocationMapper;
import com.cts.rivio.repository.LocationRepository;
import com.cts.rivio.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationResponse createLocation(LocationRequest request) {
        if (request.getName() == null || request.getTimezone() == null) {
            throw new IllegalArgumentException("Name and Timezone are mandatory");
        }

        Location location = Location.builder()
                .name(request.getName())
                .timezone(request.getTimezone())
                .currencyCode(request.getCurrencyCode() == null ? "INR" : request.getCurrencyCode())
                .build();

        Location savedLocation = locationRepository.save(location);
        return locationMapper.toResponse(savedLocation);
    }

    @Override
    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocationResponse updateLocation(Integer id, LocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));

        location.setName(request.getName());
        location.setTimezone(request.getTimezone());
        if (request.getCurrencyCode() != null) {
            location.setCurrencyCode(request.getCurrencyCode());
        }

        Location updatedLocation = locationRepository.save(location);
        return locationMapper.toResponse(updatedLocation);
    }

    @Override
    public void deleteLocation(Integer id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));

        // Acceptance Criteria: Cannot delete a location if employees are assigned to it
        if (location.getEmployees() != null && !location.getEmployees().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete location: Employees are assigned to this location.");
        }

        locationRepository.delete(location);
    }
}