package com.cts.rivio.modules.employee.entity;

import com.cts.rivio.modules.employee.enums.EmployeeStatus;
import com.cts.rivio.modules.employee.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * Mapped as Integer for now. To link to User entity later:
     * @OneToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "user_id", nullable = false, unique = true)
     * private User user;
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "bank_account", length = 20)
    private String bankAccount;

    @Column(name = "phone_no", length = 12)
    private String phoneNo;

    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "location_id", nullable = false)
     * private Location location;
     */
    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "department_id", nullable = false)
     * private Department department;
     */
    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "designation_id", nullable = false)
     * private Designation designation;
     */
    @Column(name = "designation_id", nullable = false)
    private Integer designationId;

    /*
     * Self-referencing relationship for managers:
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "reports_to_profile_id")
     * private EmployeeProfile manager;
     */
    @Column(name = "reports_to_profile_id")
    private Integer reportsToProfileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    @Builder.Default
    private EmploymentType employmentType = EmploymentType.FULL_TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.Active;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "exit_date")
    private LocalDate exitDate;
}