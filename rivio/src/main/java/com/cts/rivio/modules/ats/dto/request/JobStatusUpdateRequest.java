package com.cts.rivio.modules.ats.dto.request;

import com.cts.rivio.modules.ats.enums.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private JobStatus status;
}