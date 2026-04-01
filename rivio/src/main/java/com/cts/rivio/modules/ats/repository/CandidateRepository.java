package com.cts.rivio.modules.ats.repository;

import com.cts.rivio.modules.ats.entity.Candidate;
import com.cts.rivio.modules.ats.enums.CandidateStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // AC: Candidate email must be unique per job opening
    boolean existsByEmailAndJobOpeningId(String email, Long jobOpeningId);

    // [ATS-37] Fetch candidates by Job ID
    List<Candidate> findByJobOpeningId(Long jobOpeningId);

    // [ATS-37] Fetch candidates by Job ID and Filter by Stage
    List<Candidate> findByJobOpeningIdAndStage(Long jobOpeningId, CandidateStage stage);
}