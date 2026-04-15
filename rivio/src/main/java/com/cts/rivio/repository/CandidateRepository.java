package com.cts.rivio.repository;

import com.cts.rivio.entity.Candidate;
import com.cts.rivio.enums.CandidateStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // AC: Candidate email must be unique per job opening
    boolean existsByEmailAndJobOpeningId(String email, Integer jobOpeningId);

    // [ATS-37] Fetch candidates by Job ID
    List<Candidate> findByJobOpeningId(Integer jobOpeningId);

    boolean existsByJobOpeningId(Integer jobOpeningId);

    // [ATS-37] Fetch candidates by Job ID and Filter by Stage
    List<Candidate> findByJobOpeningIdAndStage(Integer jobOpeningId, CandidateStage stage);
}