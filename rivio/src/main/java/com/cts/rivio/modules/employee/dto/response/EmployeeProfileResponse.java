package com.cts.rivio.modules.employee.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class EmployeeProfileResponse {
    private Integer id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String departmentName;
    private String designationTitle;
    private String locationName;
    private String managerName;
    private String employmentType;
    private String status;
    private LocalDate joiningDate;
}