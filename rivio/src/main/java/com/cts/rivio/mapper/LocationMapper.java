package com.cts.rivio.mapper;

import com.cts.rivio.entity.Location;
import com.cts.rivio.dto.response.LocationResponse;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationResponse toResponse(Location location) {
        if (location == null) {
            return null;
        }
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .currencyCode(location.getCurrencyCode())
                .timezone(location.getTimezone())
                .build();
    }
}