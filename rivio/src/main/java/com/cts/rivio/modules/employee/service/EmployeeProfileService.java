package com.cts.rivio.modules.employee.service;

import com.cts.rivio.modules.employee.dto.request.EmployeeProfileRequest;
import com.cts.rivio.modules.employee.dto.response.EmployeeProfileResponse;

public interface EmployeeProfileService {
    EmployeeProfileResponse createProfile(EmployeeProfileRequest request);
}