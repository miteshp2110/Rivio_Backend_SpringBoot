package com.cts.rivio.modules.company.service;

import com.cts.rivio.modules.company.dto.request.DesignationRequest;
import com.cts.rivio.modules.company.dto.response.DesignationResponse;
import java.util.List;

public interface DesignationService {
    DesignationResponse createDesignation(DesignationRequest request);
    List<DesignationResponse> getAllDesignations(Integer departmentId); // Added for COMP-12
    DesignationResponse updateDesignation(Integer id, DesignationRequest request); // Added for COMP-12
}