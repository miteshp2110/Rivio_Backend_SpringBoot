package com.cts.rivio.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.entity.User;
import com.cts.rivio.enums.UserStatus;
import com.cts.rivio.repository.UserRepository;
import com.cts.rivio.entity.Department;
import com.cts.rivio.entity.Designation;
import com.cts.rivio.entity.Location;
import com.cts.rivio.repository.DepartmentRepository;
import com.cts.rivio.repository.DesignationRepository;
import com.cts.rivio.repository.LocationRepository;
import com.cts.rivio.dto.request.EmployeeProfileRequest;
import com.cts.rivio.dto.request.EmployeeStatusUpdateRequest;
import com.cts.rivio.dto.request.JobDetailsUpdateRequest;
import com.cts.rivio.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.dto.response.EmployeeProfileResponse;
import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.enums.EmployeeStatus;
import com.cts.rivio.enums.EmploymentType;
import com.cts.rivio.mapper.EmployeeProfileMapper;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.service.EmployeeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import com.cts.rivio.dto.request.EmployeeBasicInfoRequest;

// Added missing utility imports
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee Code '" + request.getEmployeeCode() + "' is already in use.");
        }
        if (employeeRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User ID " + request.getUserId() + " is already linked to a profile.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Department dept = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Designation desig = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new ResourceNotFoundException("Designation", "id", request.getDesignationId()));

        Location loc = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", request.getLocationId()));

        EmployeeProfile manager = null;
        if (request.getReportsToProfileId() != null) {
            manager = employeeRepository.findById(request.getReportsToProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager Profile", "id", request.getReportsToProfileId()));
        }

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
                .status(EmployeeStatus.ACTIVE)
                .build();

        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }

    // [NEW] Logic for Manual Attendance Dropdown
    @Override
    public List<Map<String, Object>> getEligibleEmployees(LocalDate date) {
        return employeeRepository.findEmployeesEligibleForAttendance(date)
                .stream()
                .map(emp -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", emp.getId());
                    map.put("name", emp.getFirstName() + " " + emp.getLastName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeProfileResponse getProfileById(Integer id) {
        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));
        return employeeMapper.toResponse(profile);
    }

    @Override
    public Page<EmployeeDirectoryResponse> getEmployeeDirectory(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
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
            if (request.getReportsToProfileId().equals(id)) {
                throw new IllegalArgumentException("An employee cannot report to themselves.");
            }
            if (request.getReportsToProfileId() == 0 || request.getReportsToProfileId() == -1) {
                profile.setManager(null);
            } else {
                EmployeeProfile newManager = employeeRepository.findById(request.getReportsToProfileId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager Profile", "id", request.getReportsToProfileId()));
                profile.setManager(newManager);
            }
        }

        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public EmployeeProfileResponse updateEmployeeStatus(Integer id, EmployeeStatusUpdateRequest request) {
        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));

        if (request.getExitDate() != null) {
            if (request.getExitDate().isBefore(LocalDate.now()) && !request.isOverridePastDate()) {
                throw new IllegalArgumentException("Exit date cannot be in the past. To backdate, you must set 'overridePastDate' to true.");
            }
            profile.setExitDate(request.getExitDate());
        } else if (request.getStatus() == EmployeeStatus.TERMINATED) {
            throw new IllegalArgumentException("An exit date must be provided when terminating an employee.");
        }

        profile.setStatus(request.getStatus());
        User user = profile.getUser();

        if (request.getStatus() == EmployeeStatus.TERMINATED) {
            user.setStatus(UserStatus.SUSPENDED);
        } else if (request.getStatus() == EmployeeStatus.ACTIVE && user.getStatus() == UserStatus.SUSPENDED) {
            user.setStatus(UserStatus.ACTIVE);
            profile.setExitDate(null);
        }

        userRepository.save(user);
        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public EmployeeProfileResponse updateBasicInfo(Integer id, EmployeeBasicInfoRequest request) {
        EmployeeProfile profile = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Profile", "id", id));

        if (request.getPhoneNo() != null) {
            profile.setPhoneNo(request.getPhoneNo());
        }
        if (request.getBankAccount() != null) {
            profile.setBankAccount(request.getBankAccount());
        }

        profile = employeeRepository.save(profile);
        return employeeMapper.toResponse(profile);
    }
}