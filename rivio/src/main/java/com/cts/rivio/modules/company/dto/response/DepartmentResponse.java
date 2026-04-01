package com.cts.rivio.modules.company.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DepartmentResponse {
    private Integer id;
    private String name;
    private Integer managerUserId;
    private String managerEmail;
}