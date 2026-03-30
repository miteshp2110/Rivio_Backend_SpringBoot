package com.cts.rivio.modules.ats.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobOpeningRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Department ID is required")
    private Integer departmentId;

    @NotNull(message = "Location ID is required")
    private Integer locationId;
}