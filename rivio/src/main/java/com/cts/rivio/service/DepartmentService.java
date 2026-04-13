package com.cts.rivio.service;

import com.cts.rivio.dto.request.DepartmentRequest;
import com.cts.rivio.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse updateDepartment(Integer id, DepartmentRequest request);
}