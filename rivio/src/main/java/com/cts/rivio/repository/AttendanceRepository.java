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
    int countByEmployeeProfileIdAndDateBetweenAndStatusIn(
            Integer employeeId, LocalDate startDate, LocalDate endDate, List<AttendanceStatus> statuses
    );
}