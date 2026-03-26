package com.cts.rivio.modules.attendance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "work_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "day_name", nullable = false, unique = true, length = 20)
    private String dayName;

    @Column(name = "is_working_day", nullable = false)
    @Builder.Default
    private Boolean isWorkingDay = true;
}