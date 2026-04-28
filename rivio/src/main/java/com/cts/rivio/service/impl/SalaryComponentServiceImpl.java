package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.SalaryComponentRequest;
import com.cts.rivio.dto.response.SalaryComponentResponse;
import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.entity.SalaryComponent;
import com.cts.rivio.mapper.SalaryComponentMapper;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.repository.SalaryComponentRepository;
import com.cts.rivio.service.SalaryComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryComponentServiceImpl implements SalaryComponentService {

    private final SalaryComponentRepository salaryComponentRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final SalaryComponentMapper salaryComponentMapper;

    @Override
    public List<SalaryComponentResponse> getComponentsByEmployee(Integer employeeId) {
        return salaryComponentRepository.findByEmployeeProfileId(employeeId)
                .stream()
                .map(salaryComponentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalaryComponentResponse addComponent(Integer employeeId, SalaryComponentRequest request) {
        EmployeeProfile employee = employeeProfileRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", employeeId));

        // Validation: Prevent duplicate component names for this specific employee
        if (salaryComponentRepository.existsByEmployeeProfileIdAndNameIgnoreCase(employeeId, request.getName())) {
            throw new IllegalArgumentException("A salary component with this name already exists for this employee.");
        }

        SalaryComponent component = SalaryComponent.builder()
                .employeeProfile(employee)
                .name(request.getName())
                .type(request.getType())
                .value(request.getValue())
                .build();

        return salaryComponentMapper.toResponse(salaryComponentRepository.save(component));
    }

    @Override
    @Transactional
    public SalaryComponentResponse updateComponent(Integer id, SalaryComponentRequest request) {
        SalaryComponent component = salaryComponentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary Component", "id", id));

        // Validation: If they are renaming it, make sure the new name doesn't conflict with another of their components
        if (salaryComponentRepository.existsByEmployeeProfileIdAndNameIgnoreCaseAndIdNot(
                component.getEmployeeProfile().getId(), request.getName(), id)) {
            throw new IllegalArgumentException("Another salary component with this name already exists for this employee.");
        }

        component.setName(request.getName());
        component.setType(request.getType());
        component.setValue(request.getValue());

        return salaryComponentMapper.toResponse(salaryComponentRepository.save(component));
    }

    @Override
    @Transactional
    public void deleteComponent(Integer id) {
        SalaryComponent component = salaryComponentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary Component", "id", id));

        // Deletion is "Safe" because if a foreign key constraint ever blocks it in the future,
        // your GlobalExceptionHandler will catch the DataIntegrityViolationException.
        salaryComponentRepository.delete(component);
    }
    @Override
    public List<SalaryComponentResponse> getAllComponents() {
        return salaryComponentRepository.findAll()
                .stream()
                .map(salaryComponentMapper::toResponse)
                .collect(Collectors.toList());
    }
}