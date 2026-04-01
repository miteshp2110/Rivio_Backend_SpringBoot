package com.cts.rivio.modules.ats.entity;

import com.cts.rivio.modules.ats.enums.JobStatus;
import com.cts.rivio.modules.company.entity.Department;
import com.cts.rivio.modules.company.entity.Location;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department; // Now correctly refers to ats.entity.Department

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;     // Now correctly refers to ats.entity.Location

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidate> candidates;
}