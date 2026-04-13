package com.cts.rivio.entity;

import com.cts.rivio.enums.PayslipStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(
        name = "payslips",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"pay_cycle_id", "employee_profile_id"})
        }
)
@Data
public class PaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pay_cycle_id", nullable = false)
    private PayCycle payCycle;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_profile_id", nullable = false)
    private EmployeeProfile employeeProfile;

    @Column(name = "gross_earnings", precision = 15, scale = 2, nullable = false)
    private BigDecimal grossEarnings;

    @Column(name = "total_deductions", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalDeductions;

    @Column(name = "net_pay", precision = 15, scale = 2, nullable = false)
    private BigDecimal netPay;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayslipStatus status;
}