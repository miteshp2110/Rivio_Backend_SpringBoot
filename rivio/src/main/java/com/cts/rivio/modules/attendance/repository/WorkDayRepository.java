package com.cts.rivio.modules.attendance.repository;

import com.cts.rivio.modules.attendance.entity.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Integer> {
    Optional<WorkDay> findByDayName(String dayName);
}