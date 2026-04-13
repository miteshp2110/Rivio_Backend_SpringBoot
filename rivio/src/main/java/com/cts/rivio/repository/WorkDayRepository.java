package com.cts.rivio.repository;

import com.cts.rivio.entity.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Integer> {
    Optional<WorkDay> findByDayName(String dayName);
}