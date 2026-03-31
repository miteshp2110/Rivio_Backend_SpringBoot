package com.cts.rivio.modules.employee.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.auth.entity.User;
import com.cts.rivio.modules.auth.repository.UserRepository;
import com.cts.rivio.modules.company.entity.Department;
import com.cts.rivio.modules.company.entity.Designation;
import com.cts.rivio.modules.company.entity.Location;
import com.cts.rivio.modules.company.repository.DepartmentRepository;
import com.cts.rivio.modules.company.repository.DesignationRepository;
import com.cts.rivio.modules.company.repository.LocationRepository;
import com.cts.rivio.modules.employee.dto.request.EmployeeProfileRequest;
import com.cts.rivio.modules.employee.dto.response.EmployeeProfileResponse;
import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.employee.enums.EmployeeStatus;
import com.cts.rivio.modules.employee.enums.EmploymentType;
import com.cts.rivio.modules.employee.mapper.EmployeeProfileMapper;
import com.cts.rivio.modules.employee.repository.EmployeeProfileRepository;
import com.cts.rivio.modules.employee.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final LocationRepository locationRepository;
    private final EmployeeProfileMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeProfileResponse createProfile(EmployeeProfileRequest request) {

        // AC 1: Validate Unique Constraints
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee Code '" + request.getEmployeeCode() + "' is already in use.");
        }
        if (employeeRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User ID " + request.getUserId() + " is already linked to a profile.");
        }

        // AC 2: Validate ALL Foreign Keys
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Designation desig = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", request.getDesignationId()));

        Location loc = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", request.getLocationId()));

        // Validate Manager (if provided)
        EmployeeProfile manager = null;
        if (request.getReportsToProfileId() != null) {
            manager = employeeRepository.findById(request.getReportsToProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager Profile", "id", request.getReportsToProfileId()));
        }

        // Build Entity
        EmployeeProfile profile = EmployeeProfile.builder()
                .user(user)
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(dept)
                .designation(desig)
                .location(loc)
                .manager(manager)
                .joiningDate(request.getJoiningDate())
                .employmentType(request.getEmploymentType() != null ? request.getEmploymentType() : EmploymentType.FULL_TIME)
                .status(EmployeeStatus.ACTIVE) // AC 3: Default status is Active
                .build();

        // Save and map to response
        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }
}