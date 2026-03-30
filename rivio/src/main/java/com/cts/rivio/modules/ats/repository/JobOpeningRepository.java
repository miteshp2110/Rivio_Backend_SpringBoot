package com.cts.rivio.modules.ats.repository;

import com.cts.rivio.modules.ats.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {
}