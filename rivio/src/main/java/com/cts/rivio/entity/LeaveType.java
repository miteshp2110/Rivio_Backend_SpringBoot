package com.cts.rivio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "leave_types")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, unique = true, length = 50) // Fix 1: Unique constraint
    private String name;

    @DecimalMin(value = "0.1", message = "Yearly allotment must be greater than 0") // Fix 2: Allotment > 0
    @Column(name = "yearly_allotment", nullable = false, precision = 5, scale = 2)
    private BigDecimal yearlyAllotment;

    @Builder.Default // Fix 3: Ensures default value works with Lombok Builder
    @Column(name = "carry_forward_limit", precision = 5, scale = 2)
    private BigDecimal carryForwardLimit = BigDecimal.ZERO;

    @JsonIgnore // Fix 4: Prevents infinite loop in API response
    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<EmployeeLeaveBalance> leaveBalances;

    @JsonIgnore // Fix 4: Prevents infinite loop in API response
    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;
}