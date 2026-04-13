package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.entity.User;
import com.cts.rivio.repository.UserRepository;
import com.cts.rivio.dto.request.DepartmentRequest;
import com.cts.rivio.dto.response.DepartmentResponse;
import com.cts.rivio.entity.Department;
import com.cts.rivio.mapper.DepartmentMapper;
import com.cts.rivio.repository.DepartmentRepository;
import com.cts.rivio.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Department name must be unique");
        }

        User manager = null;
        if (request.getManagerUserId() != null) {
            manager = userRepository.findById(request.getManagerUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getManagerUserId()));
        }

        Department department = Department.builder()
                .name(request.getName())
                .manager(manager)
                .build();

        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse updateDepartment(Integer id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Another department with this name already exists");
        }

        User manager = null;
        if (request.getManagerUserId() != null) {
            manager = userRepository.findById(request.getManagerUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getManagerUserId()));
        }

        department.setName(request.getName());
        department.setManager(manager);

        return departmentMapper.toResponse(departmentRepository.save(department));
    }
}