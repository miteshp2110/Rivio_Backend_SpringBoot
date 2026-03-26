package com.cts.rivio.modules.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String module; // e.g., Payroll, Leave

    @Column(name = "key_name", nullable = false, unique = true, length = 100)
    private String keyName; // e.g., APPROVE_PAYROLL
}