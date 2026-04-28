package com.cts.rivio.dto.response;

import com.cts.rivio.enums.ComponentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalaryComponentResponse {
    private Integer id;

    // Employee details
    private Integer employeeProfileId;
    private String employeeName; // <-- NEW

    // Component details
    private String name;
    private ComponentType type;
    private BigDecimal value;
}