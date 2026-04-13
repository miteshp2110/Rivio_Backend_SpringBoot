package com.cts.rivio.repository;

import com.cts.rivio.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    boolean existsByDate(LocalDate date);

    // AC 1: Fetch all holidays sorted chronologically
    List<Holiday> findAllByOrderByDateAsc();
}