package com.cts.rivio.repository;

import com.cts.rivio.entity.Attendance;
import com.cts.rivio.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByEmployeeProfileIdAndDate(Integer employeeProfileId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);

    // For specific reports (Your existing code)
    int countByEmployeeProfileIdAndDateBetweenAndStatusIn(
            Integer employeeId, LocalDate startDate, LocalDate endDate, List<AttendanceStatus> statuses
    );

    // [NEW for ATTN-13] Fetch actual records for the history view
    List<Attendance> findByEmployeeProfileIdAndDateBetweenOrderByDateDesc(
            Integer employeeId, LocalDate startDate, LocalDate endDate
    );

    // Add these to calculate daily attendance rates
    long countByDateAndStatus(java.time.LocalDate date, com.cts.rivio.enums.AttendanceStatus status);
    List<com.cts.rivio.entity.Attendance> findByDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}