package com.cts.rivio.service;

import com.cts.rivio.dto.request.DesignationRequest;
import com.cts.rivio.dto.response.DesignationResponse;
import java.util.List;

public interface DesignationService {
    DesignationResponse createDesignation(DesignationRequest request);
    List<DesignationResponse> getAllDesignations(Integer departmentId); // Added for COMP-12
    DesignationResponse updateDesignation(Integer id, DesignationRequest request); // Added for COMP-12
}