package com.cts.rivio.modules.employee.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.auth.entity.User;
import com.cts.rivio.modules.auth.enums.UserStatus;
import com.cts.rivio.modules.auth.repository.UserRepository;
import com.cts.rivio.modules.company.entity.Department;
import com.cts.rivio.modules.company.entity.Designation;
import com.cts.rivio.modules.company.entity.Location;
import com.cts.rivio.modules.company.repository.DepartmentRepository;
import com.cts.rivio.modules.company.repository.DesignationRepository;
import com.cts.rivio.modules.company.repository.LocationRepository;
import com.cts.rivio.modules.employee.dto.request.EmployeeProfileRequest;
import com.cts.rivio.modules.employee.dto.request.EmployeeStatusUpdateRequest;
import com.cts.rivio.modules.employee.dto.request.JobDetailsUpdateRequest;
import com.cts.rivio.modules.employee.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.modules.employee.dto.response.EmployeeProfileResponse;
import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import com.cts.rivio.modules.employee.enums.EmployeeStatus;
import com.cts.rivio.modules.employee.enums.EmploymentType;
import com.cts.rivio.modules.employee.mapper.EmployeeProfileMapper;
import com.cts.rivio.modules.employee.repository.EmployeeProfileRepository;
import com.cts.rivio.modules.employee.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final LocationRepository locationRepository;
    private final EmployeeProfileMapper employeeMapper;
    private final ObjectMapper objectMapper;

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

    @Override
    public EmployeeProfileResponse getProfileById(Integer id) {

        // AC 1: Returns 404 if not found (Handled by GlobalExceptionHandler)
        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));

        return employeeMapper.toResponse(profile);
    }

    @Override
    public Page<EmployeeDirectoryResponse> getEmployeeDirectory(String search, int page, int size) {

        // AC 2: Must be paginated (sorted by first name alphabetically)
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());

        // AC 1: Fetch active employees and apply optional search filter
        Page<EmployeeProfile> profilePage = employeeRepository.searchActiveEmployees(
                search,
                EmployeeStatus.ACTIVE,
                pageable
        );
        return profilePage.map(employeeMapper::toDirectoryResponse);
    }

    @Override
    @Transactional
    public EmployeeProfileResponse updateJobDetails(Integer id, JobDetailsUpdateRequest request) {

        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));


        // --- 2. AC 1: Validate & Apply New FKs ONLY if they were provided in the request ---

        if (request.getDepartmentId() != null) {
            Department newDept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            profile.setDepartment(newDept);
        }

        if (request.getDesignationId() != null) {
            Designation newDesig = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", request.getDesignationId()));
            profile.setDesignation(newDesig);
        }

        if (request.getLocationId() != null) {
            Location newLoc = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location", "id", request.getLocationId()));
            profile.setLocation(newLoc);
        }

        if (request.getReportsToProfileId() != null) {
            // Prevent circular reporting (an employee cannot be their own manager)
            if (request.getReportsToProfileId().equals(id)) {
                throw new IllegalArgumentException("An employee cannot report to themselves.");
            }

            // Special case: Allow passing '0' or '-1' if HR explicitly wants to REMOVE a manager
            if (request.getReportsToProfileId() == 0 || request.getReportsToProfileId() == -1) {
                profile.setManager(null);
            } else {
                EmployeeProfile newManager = employeeRepository.findById(request.getReportsToProfileId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager Profile", "id", request.getReportsToProfileId()));
                profile.setManager(newManager);
            }
        }

        // Save the updated profile
        profile = employeeRepository.save(profile);



        return employeeMapper.toResponse(profile);
    }


    @Override
    @Transactional // AC 2: Ensures BOTH the profile and the user account update together, or roll back if one fails.
    public EmployeeProfileResponse updateEmployeeStatus(Integer id, EmployeeStatusUpdateRequest request) {

        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));

        // --- 1. Validate Exit Date (AC 1) ---
        if (request.getExitDate() != null) {
            if (request.getExitDate().isBefore(LocalDate.now()) && !request.isOverridePastDate()) {
                throw new IllegalArgumentException("Exit date cannot be in the past. To backdate, you must set 'overridePastDate' to true.");
            }
            profile.setExitDate(request.getExitDate());
        } else if (request.getStatus() == EmployeeStatus.TERMINATED) {
            // Enforce that a terminated employee MUST have an exit date
            throw new IllegalArgumentException("An exit date must be provided when terminating an employee.");
        }

        // --- 2. Update Profile Status ---
        profile.setStatus(request.getStatus());

        // --- 3. Manage Application Login Access (AC 2) ---
        User user = profile.getUser();

        if (request.getStatus() == EmployeeStatus.TERMINATED) {
            // Suspend credentials immediately so they cannot log in
            user.setStatus(UserStatus.SUSPENDED);

        } else if (request.getStatus() == EmployeeStatus.ACTIVE && user.getStatus() == UserStatus.SUSPENDED) {
            // If HR made a mistake and reactivates them, restore their login access
            user.setStatus(UserStatus.ACTIVE);
            profile.setExitDate(null); // Clear the exit date
        }

        // Save the user (Hibernate will track this, but explicitly saving is good practice)
        userRepository.save(user);

        // Save the profile and return the flattened DTO
        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }
}