package com.cts.rivio.repository;

import com.cts.rivio.entity.EmployeeLeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Integer> {

    /**
     * [LEAV-22] Fetch all leave balances for a specific employee and year.
     * Note: We use 'employeeProfileId' because the field in the entity is 'employeeProfile'.
     */
    List<EmployeeLeaveBalance> findByEmployeeProfileIdAndYear(Integer employeeProfileId, Integer year);

    /**
     * [LEAV-21] Used for Idempotency check.
     * Finds a specific balance record to prevent duplicate allocations.
     */
    Optional<EmployeeLeaveBalance> findByEmployeeProfileIdAndLeaveTypeIdAndYear(
            Integer employeeProfileId,
            Integer leaveTypeId,
            Integer year
    );

    boolean existsByLeaveTypeId(Integer leaveTypeId);

    /**
     * [LEAV-21] Quick check to see if any balances exist for an employee in a given year.
     */
    boolean existsByEmployeeProfileIdAndYear(Integer employeeProfileId, Integer year);
}