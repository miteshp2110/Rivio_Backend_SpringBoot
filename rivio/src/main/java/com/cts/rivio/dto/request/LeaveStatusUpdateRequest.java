package com.cts.rivio.dto.request;

import com.cts.rivio.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private LeaveStatus status;
}