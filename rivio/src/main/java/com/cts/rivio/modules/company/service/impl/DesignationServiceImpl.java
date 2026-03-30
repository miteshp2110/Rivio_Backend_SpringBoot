package com.cts.rivio.modules.company.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.company.dto.request.DesignationRequest;
import com.cts.rivio.modules.company.dto.response.DesignationResponse;
import com.cts.rivio.modules.company.entity.Department;
import com.cts.rivio.modules.company.entity.Designation;
import com.cts.rivio.modules.company.mapper.DesignationMapper;
import com.cts.rivio.modules.company.repository.DepartmentRepository;
import com.cts.rivio.modules.company.repository.DesignationRepository;
import com.cts.rivio.modules.company.service.DesignationService;
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
        designation.setTitle(request.getTitle());

        // Update Department if a new one is provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            designation.setDepartment(department);
        }

        return designationMapper.toResponse(designationRepository.save(designation));
    }
}