package com.cts.rivio.dto.response;

import com.cts.rivio.enums.ComponentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryComponentResponse {
    private Integer id;
    private String name;
    private ComponentType type;
    private BigDecimal value;
}