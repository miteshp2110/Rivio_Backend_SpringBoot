package com.cts.rivio.modules.employee.dto.request;

import com.cts.rivio.modules.employee.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeProfileRequest {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Employee Code is required")
    private String employeeCode;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotNull(message = "Department ID is required")
    private Integer departmentId;

    @NotNull(message = "Designation ID is required")
    private Integer designationId;

    @NotNull(message = "Location ID is required")
    private Integer locationId;

    private Integer reportsToProfileId; // Optional Manager

    @NotNull(message = "Joining Date is required")
    private LocalDate joiningDate;

    private EmploymentType employmentType;
}