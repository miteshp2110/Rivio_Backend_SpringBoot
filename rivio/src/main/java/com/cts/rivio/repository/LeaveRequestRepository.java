package com.cts.rivio.repository;

import com.cts.rivio.entity.LeaveRequest;
import com.cts.rivio.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    // FIX: Explicitly tell Spring how to find the manager via the employee relationship
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.manager.id = :managerId AND lr.status = :status")
    List<LeaveRequest> findByManagerIdAndStatus(@Param("managerId") Integer managerId, @Param("status") LeaveStatus status);

    // Existing ATTN-33 method
    @Query("SELECT COUNT(l) > 0 FROM LeaveRequest l WHERE l.employee.id = :empId AND l.status = 'APPROVED' AND :targetDate BETWEEN l.startDate AND l.endDate")
    boolean hasApprovedLeaveOnDate(@Param("empId") Integer empId, @Param("targetDate") LocalDate targetDate);
}