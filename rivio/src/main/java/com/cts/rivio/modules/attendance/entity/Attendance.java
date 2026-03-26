package com.cts.rivio.modules.attendance.entity;

import com.cts.rivio.modules.attendance.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_date",
                        columnNames = {"employee_profile_id", "date"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* * NOTE: Mapped as a basic column for now.
     * Once you create the EmployeeProfile entity, you can change this to:
     * * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "employee_profile_id", nullable = false)
     * private EmployeeProfile employeeProfile;
     */
    @Column(name = "employee_profile_id", nullable = false)
    private Integer employeeProfileId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "punch_in")
    private LocalDateTime punchIn;

    @Column(name = "punch_out")
    private LocalDateTime punchOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.Present;

    /*
     * NOTE: Mapped as a basic column for now.
     * Once you create the User entity, you can change this to:
     * * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "created_by_user_id")
     * private User createdByUser;
     */
    @Column(name = "created_by_user_id")
    private Integer createdByUserId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}