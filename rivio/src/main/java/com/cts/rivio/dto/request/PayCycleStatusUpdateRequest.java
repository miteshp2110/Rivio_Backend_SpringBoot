package com.cts.rivio.dto.request;

import com.cts.rivio.enums.PayCycleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayCycleStatusUpdateRequest {

    @NotNull(message = "New status is required")
    private PayCycleStatus status;
}