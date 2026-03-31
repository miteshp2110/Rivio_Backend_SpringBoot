package com.cts.rivio.modules.employee.service;

import com.cts.rivio.modules.employee.dto.request.EmployeeProfileRequest;
import com.cts.rivio.modules.employee.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.modules.employee.dto.response.EmployeeProfileResponse;
import org.springframework.data.domain.Page;

public interface EmployeeProfileService {
    EmployeeProfileResponse createProfile(EmployeeProfileRequest request);
    EmployeeProfileResponse getProfileById(Integer id);
    Page<EmployeeDirectoryResponse> getEmployeeDirectory(String search, int page, int size);
    EmployeeProfileResponse updateJobDetails(Integer id, com.cts.rivio.modules.employee.dto.request.JobDetailsUpdateRequest request);
}