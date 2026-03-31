package com.cts.rivio.modules.employee.dto.request;

import lombok.Data;

@Data
public class JobDetailsUpdateRequest {


    private Integer departmentId;

    private Integer designationId;

    private Integer locationId;

    private Integer reportsToProfileId;
}