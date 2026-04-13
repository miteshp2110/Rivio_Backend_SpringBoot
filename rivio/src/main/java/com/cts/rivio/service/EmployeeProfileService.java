package com.cts.rivio.service;

import com.cts.rivio.dto.request.EmployeeBasicInfoRequest;
import com.cts.rivio.dto.request.EmployeeProfileRequest;
import com.cts.rivio.dto.request.JobDetailsUpdateRequest;
import com.cts.rivio.dto.request.EmployeeStatusUpdateRequest;
import com.cts.rivio.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.dto.response.EmployeeProfileResponse;
import org.springframework.data.domain.Page;

public interface EmployeeProfileService {
    EmployeeProfileResponse createProfile(EmployeeProfileRequest request);
    EmployeeProfileResponse getProfileById(Integer id);
    Page<EmployeeDirectoryResponse> getEmployeeDirectory(String search, int page, int size);
    EmployeeProfileResponse updateJobDetails(Integer id, JobDetailsUpdateRequest request);
    EmployeeProfileResponse updateEmployeeStatus(Integer id, EmployeeStatusUpdateRequest request);
    EmployeeProfileResponse updateBasicInfo(Integer id, EmployeeBasicInfoRequest request);
}