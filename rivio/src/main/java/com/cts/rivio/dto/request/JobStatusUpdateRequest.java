package com.cts.rivio.dto.request;

import com.cts.rivio.enums.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private JobStatus status;
}