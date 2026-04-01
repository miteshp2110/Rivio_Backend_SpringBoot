package com.cts.rivio.modules.employee.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDirectoryResponse {
    private Integer id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentName;
    private String designationTitle;
}