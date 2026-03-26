package com.cts.rivio.modules.ats.entity;

import com.cts.rivio.modules.ats.enums.CandidateStage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_opening_id", nullable = false)
    private JobOpening jobOpening; // Many-to-One relationship from schema

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "resume_url")
    private String resumeUrl; // Matches schema column

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandidateStage stage;
}