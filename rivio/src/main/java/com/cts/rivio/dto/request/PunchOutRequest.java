package com.cts.rivio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PunchOutRequest {
    @NotNull(message = "Punch Out time is required")
    private LocalDateTime punchOut;
}