package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class EmployeeProfileResponse {
    private Integer id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Integer userId;
    private String departmentName;
    private String designationTitle;
    private String locationName;
    private String managerName;
    private String employmentType;
    private String status;
    private LocalDate joiningDate;
    private String phoneNo;
    private String bankAccount;
    private List<SalaryComponentResponse> salaryComponents;
}