package com.cts.rivio.modules.leave.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class LeaveBalanceResponse {
    private Integer id;
    private String leaveTypeName; // Flattened from LeaveType entity
    private Integer year;
    private BigDecimal allotted;
    private BigDecimal consumed;
    private BigDecimal balance;   // Comes from the DB @Formula
}