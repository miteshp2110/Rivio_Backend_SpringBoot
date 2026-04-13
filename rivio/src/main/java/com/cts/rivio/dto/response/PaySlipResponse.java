package com.cts.rivio.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaySlipResponse {
    private Long id;
    private Integer payCycleId;
    private Integer employeeProfileId;
    private String employeeName;
    private String employeeCode;
    private BigDecimal grossEarnings;
    private BigDecimal totalDeductions;
    private BigDecimal netPay;
}