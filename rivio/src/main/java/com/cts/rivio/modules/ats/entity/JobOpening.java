package com.cts.rivio.modules.ats.entity;

import com.cts.rivio.modules.ats.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "job_openings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId; // Links to Module 2

    @Column(name = "location_id", nullable = false)
    private Integer locationId; // Links to Module 2

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidate> candidates;
}