//package com.cts.rivio.dto.response;
//
//import lombok.Data;
//import java.math.BigDecimal;
//
//@Data
//public class PaySlipResponse {
//    private Long id;
//    private Integer payCycleId;
//    private Integer employeeProfileId;
//    private String employeeName;
//    private String employeeCode;
//    private BigDecimal grossEarnings;
//    private BigDecimal totalDeductions;
//    private BigDecimal netPay;
//}

package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PaySlipResponse {
    private Integer id;

    // Employee Info
    private Integer employeeProfileId;
    private String employeeName;
    private String employeeCode;

    // Financial Info
    private Double grossEarnings;
    private Double totalDeductions;
    private Double netPay;

    // --- NEW: Pay Cycle Details ---
    private Integer payCycleId;
    private String cycleName;
    private LocalDate cycleFromDate;
    private LocalDate cycleToDate;
    private String cycleStatus; // DRAFT, PROCESSING, FINALIZED, or PAID
}