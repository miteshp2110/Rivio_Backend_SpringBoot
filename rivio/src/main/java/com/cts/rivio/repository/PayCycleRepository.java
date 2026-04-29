package com.cts.rivio.repository;

import com.cts.rivio.entity.PayCycle;
import com.cts.rivio.enums.PayCycleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayCycleRepository extends JpaRepository<PayCycle, Integer> {

    @Query("SELECT COUNT(p) > 0 FROM PayCycle p WHERE p.status = :status " +
            "AND (p.fromDate <= :toDate AND p.toDate >= :fromDate)")
    boolean hasOverlappingCycle(@Param("status") PayCycleStatus status,
                                @Param("fromDate") LocalDate fromDate,
                                @Param("toDate") LocalDate toDate);

    // --- NEW SIMPLE SEARCH METHOD ---
    // Spring automatically writes the SQL: SELECT * WHERE cycle_name LIKE %?%
    List<PayCycle> findByCycleNameContainingIgnoreCase(String cycleName);

    // Add this to count active payroll runs
    long countByStatusIn(java.util.List<com.cts.rivio.enums.PayCycleStatus> statuses);
}