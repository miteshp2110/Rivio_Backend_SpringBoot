package com.cts.rivio.modules.leave.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "yearly_allotment", nullable = false, precision = 5, scale = 2)
    private BigDecimal yearlyAllotment;

    @Column(name = "carry_forward_limit", precision = 5, scale = 2)
    private BigDecimal carryForwardLimit = BigDecimal.ZERO;

    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<EmployeeLeaveBalance> leaveBalances;

    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;
}
