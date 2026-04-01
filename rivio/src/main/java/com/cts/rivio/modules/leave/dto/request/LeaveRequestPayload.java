package com.cts.rivio.modules.leave.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestPayload {
    @NotNull(message = "Employee ID is required")
    private Integer employeeProfileId;

    @NotNull(message = "Leave Type ID is required")
    private Integer leaveTypeId;

    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    private LocalDate endDate;

    @NotNull(message = "Days requested is required")
    @DecimalMin(value = "0.5", message = "Minimum leave is 0.5 days")
    private Double daysRequested;
}