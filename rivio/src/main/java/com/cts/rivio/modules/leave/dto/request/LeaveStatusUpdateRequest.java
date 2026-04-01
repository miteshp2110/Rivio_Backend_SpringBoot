package com.cts.rivio.modules.leave.dto.request;

import com.cts.rivio.modules.leave.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private LeaveStatus status;
}