package com.cts.rivio.modules.company.service;

import com.cts.rivio.modules.company.dto.request.DepartmentRequest;
import com.cts.rivio.modules.company.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse updateDepartment(Integer id, DepartmentRequest request);
}