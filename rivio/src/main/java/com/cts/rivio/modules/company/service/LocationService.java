package com.cts.rivio.modules.company.service;

import com.cts.rivio.modules.company.dto.request.LocationRequest;
import com.cts.rivio.modules.company.dto.response.LocationResponse;
import java.util.List; // Required for fetching the list of locations

public interface LocationService {

    // [COMP-08] Create Location (First User Story)
    LocationResponse createLocation(LocationRequest request);

    // [COMP-09] List Locations (Second User Story)
    List<LocationResponse> getAllLocations();

    // [COMP-08] Edit Location (Second User Story)
    LocationResponse updateLocation(Integer id, LocationRequest request);

    // [COMP-08] Delete Location (Additional Requirement)
    void deleteLocation(Integer id);
}