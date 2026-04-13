package com.cts.rivio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkDayRequest {
    @NotNull(message = "isWorkingDay flag is required")
    private Boolean isWorkingDay;
}