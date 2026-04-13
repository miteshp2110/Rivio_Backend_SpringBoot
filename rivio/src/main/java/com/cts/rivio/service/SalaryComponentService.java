package com.cts.rivio.service;

import com.cts.rivio.dto.request.SalaryComponentRequest;
import com.cts.rivio.dto.response.SalaryComponentResponse;

import java.util.List;

public interface SalaryComponentService {
    List<SalaryComponentResponse> getComponentsByEmployee(Integer employeeId);
    SalaryComponentResponse addComponent(Integer employeeId, SalaryComponentRequest request);
    SalaryComponentResponse updateComponent(Integer id, SalaryComponentRequest request);
    void deleteComponent(Integer id);
}