package com.cts.rivio.modules.company.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DesignationResponse {
    private Integer id;
    private String title;
    private Integer departmentId;
    private String departmentName;
}