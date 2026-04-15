package com.cts.rivio.repository;

import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {

    // AC 1: Ensure Employee Code is unique
    boolean existsByEmployeeCode(String employeeCode);

    // Ensure the User isn't already linked to another profile
    boolean existsByUserId(Integer userId);

    List<EmployeeProfile> findByStatus(EmployeeStatus status);

    @Query("SELECT e FROM EmployeeProfile e WHERE e.status = :status AND " +
            "(:search IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EmployeeProfile> searchActiveEmployees(@Param("search") String search,
                                                @Param("status") EmployeeStatus status,
                                                Pageable pageable);

    /**
     * Logic for Manual Attendance Dropdown:
     * Finds employees who are NOT Terminated AND do NOT have an approved leave on the selected date.
     */
    @Query("SELECT e FROM EmployeeProfile e WHERE e.status <> com.cts.rivio.enums.EmployeeStatus.TERMINATED " +
            "AND NOT EXISTS (SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = e.id " +
            "AND lr.status = com.cts.rivio.enums.LeaveStatus.APPROVED " +
            "AND :date BETWEEN lr.startDate AND lr.endDate)")
    List<EmployeeProfile> findEmployeesEligibleForAttendance(@Param("date") LocalDate date);
}
//    @Query("SELECT e FROM EmployeeProfile e WHERE e.status <> com.cts.rivio.enums.EmployeeStatus.TERMINATED " +
//            "AND NOT EXISTS (SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = e.id " +
//            "AND lr.status = com.cts.rivio.enums.LeaveStatus.APPROVED " +
//            "AND :date BETWEEN lr.startDate AND lr.endDate)")
//    List<EmployeeProfile> findEmployeesEligibleForAttendance(@Param("date") LocalDate date);
//}