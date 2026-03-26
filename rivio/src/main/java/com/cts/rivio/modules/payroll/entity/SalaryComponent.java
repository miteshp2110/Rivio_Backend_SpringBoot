package com.cts.rivio.modules.payroll.entity;
import com.cts.rivio.modules.payroll.enums.ComponentType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "salary_components")
@Data
public class SalaryComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    private BigDecimal value;
}