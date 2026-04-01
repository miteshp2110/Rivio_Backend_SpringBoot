package com.cts.rivio.modules.attendance.repository;

import com.cts.rivio.modules.attendance.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

// 1. We extend JpaRepository so we don't have to write SQL for save/delete.
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    // 2. This method follows a naming convention.
    // Spring will automatically write: "SELECT count(*) FROM holidays WHERE date = ?"
    boolean existsByDate(LocalDate date);
}