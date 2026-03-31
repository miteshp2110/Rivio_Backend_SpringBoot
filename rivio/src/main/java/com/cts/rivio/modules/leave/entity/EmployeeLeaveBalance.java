package com.cts.rivio.modules.leave.entity;

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;

@Entity
@Table(
        name = "employee_leave_balances",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_profile_id", "leave_type_id", "year"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeLeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_profile_id", nullable = false)
    private EmployeeProfile employeeProfile;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false, columnDefinition = "YEAR")
    private Integer year;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal allotted = BigDecimal.ZERO;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal consumed = BigDecimal.ZERO;

    // Mirrors the GENERATED ALWAYS AS (allotted - consumed) STORED column
    @Formula("allotted - consumed")
    private BigDecimal balance;
}

