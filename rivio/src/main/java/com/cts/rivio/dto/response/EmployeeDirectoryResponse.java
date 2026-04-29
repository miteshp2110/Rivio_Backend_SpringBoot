//package com.cts.rivio.dto.response;
//
//import lombok.Builder;
//import lombok.Data;
//
//@Data
//@Builder
//public class EmployeeDirectoryResponse {
//    private Integer id;
//    private String employeeCode;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String departmentName;
//    private String designationTitle;
//}

package com.cts.rivio.dto.response;

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

    // NEW: Add the Role Name
    private String roleName;
}