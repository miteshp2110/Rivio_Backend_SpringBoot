package com.cts.rivio.dto.response;

import com.cts.rivio.enums.JobStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobOpeningResponse {
    private Integer id;
    private String title;
    private JobStatus status;

    // Flattened Department details
    private Integer departmentId;
    private String departmentName;

    // Flattened Location details
    private Integer locationId;
    private String locationName;
}