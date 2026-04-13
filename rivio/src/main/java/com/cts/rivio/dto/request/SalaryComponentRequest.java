package com.cts.rivio.dto.request;

import com.cts.rivio.enums.ComponentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryComponentRequest {

    @NotBlank(message = "Component name is required")
    private String name;

    @NotNull(message = "Component type (EARNING/DEDUCTION) is required")
    private ComponentType type;

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0")
    private BigDecimal value;
}