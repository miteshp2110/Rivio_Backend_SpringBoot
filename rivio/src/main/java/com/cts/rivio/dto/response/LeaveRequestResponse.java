package com.cts.rivio.dto.response;

import com.cts.rivio.enums.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponse {

    private Integer id;

    // Foreign Key IDs
    private Integer employeeProfileId;
    private Integer leaveTypeId;

    // Display Names for the frontend
    private String employeeName;
    private String leaveTypeName;

    // Leave Details
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal daysRequested;

    // FIX: Using the Enum here to match your Entity and Service layers!
    private LeaveStatus status;

    // [NEW] Added for the HR Admin Auditing feature
    private Integer approvedByProfileId;
}