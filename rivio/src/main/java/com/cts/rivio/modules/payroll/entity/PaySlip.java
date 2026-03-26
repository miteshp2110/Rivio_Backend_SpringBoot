package com.cts.rivio.modules.payroll.entity;
import com.cts.rivio.modules.payroll.entity.PayCycle;
import com.cts.rivio.modules.payroll.enums.PayslipStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "payslips")
@Data
public class PaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pay_cycle_id")
    private PayCycle payCycle;

    @Column(name = "gross_earnings", precision = 12, scale = 2)
    private BigDecimal grossEarnings;

    @Column(name = "total_deductions", precision = 12, scale = 2)
    private BigDecimal totalDeductions;

    @Column(name = "net_pay", precision = 12, scale = 2)
    private BigDecimal netPay;

    @Enumerated(EnumType.STRING)
    private PayslipStatus status;
}