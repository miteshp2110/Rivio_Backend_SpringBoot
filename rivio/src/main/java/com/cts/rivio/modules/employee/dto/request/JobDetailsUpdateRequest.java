package com.cts.rivio.modules.employee.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobDetailsUpdateRequest {

    @NotNull(message = "Department ID is required")
    private Integer departmentId;

    @NotNull(message = "Designation ID is required")
    private Integer designationId;

    @NotNull(message = "Location ID is required")
    private Integer locationId;

    private Integer reportsToProfileId; // Optional: Can be null if they report to no one (e.g., CEO)
}