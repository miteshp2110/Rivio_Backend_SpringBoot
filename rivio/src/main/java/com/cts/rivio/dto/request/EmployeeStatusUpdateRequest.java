package com.cts.rivio.dto.request;

import com.cts.rivio.enums.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeStatusUpdateRequest {

    @NotNull(message = "Employee status is required")
    private EmployeeStatus status;

    private LocalDate exitDate;

    // AC 1: Defaults to false. If an admin tries to backdate a termination,
    // they must explicitly pass this as true in the JSON payload.
    private boolean overridePastDate = false;
}