package com.cts.rivio.modules.attendance.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceRequest {
    @NotNull(message = "Employee Profile ID is required")
    private Integer employeeProfileId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalDateTime punchIn;
    private LocalDateTime punchOut;
}