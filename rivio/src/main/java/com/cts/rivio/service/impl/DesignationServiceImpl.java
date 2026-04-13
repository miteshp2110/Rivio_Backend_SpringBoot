package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.DesignationRequest;
import com.cts.rivio.dto.response.DesignationResponse;
import com.cts.rivio.entity.Department;
import com.cts.rivio.entity.Designation;
import com.cts.rivio.mapper.DesignationMapper;
import com.cts.rivio.repository.DepartmentRepository;
import com.cts.rivio.repository.DesignationRepository;
import com.cts.rivio.service.DesignationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationMapper designationMapper;

    @Override
    public DesignationResponse createDesignation(DesignationRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Designation designation = Designation.builder()
                .title(request.getTitle())
                .department(department)
                .build();

        return designationMapper.toResponse(designationRepository.save(designation));
    }

    @Override
    public List<DesignationResponse> getAllDesignations(Integer departmentId) {
        List<Designation> designations;

        // Acceptance Criteria: Support filtering by departmentId
        if (departmentId != null) {
            designations = designationRepository.findAll().stream()
                    .filter(d -> d.getDepartment().getId().equals(departmentId))
                    .collect(Collectors.toList());
        } else {
            designations = designationRepository.findAll();
        }

        return designations.stream()
                .map(designationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DesignationResponse updateDesignation(Integer id, DesignationRequest request) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", id));

        // Update Title
        if (request.getTitle() != null) {
            designation.setTitle(request.getTitle());
        }

        // Update Department if a new one is provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            designation.setDepartment(department);
        }

        return designationMapper.toResponse(designationRepository.save(designation));
    }

    @Override
    @Transactional
    public void deleteDesignation(Integer id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", id));

        // Acceptance Criteria 1: Safe Delete Check
        // Block deletion if any employee (active or past) is linked to this designation
        if (designation.getEmployees() != null && !designation.getEmployees().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete designation: One or more employees are currently or were previously assigned to this title.");
        }

        designationRepository.delete(designation);
    }
}