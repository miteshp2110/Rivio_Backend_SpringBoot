package com.cts.rivio.repository;

import com.cts.rivio.entity.LeaveRequest;
import com.cts.rivio.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    boolean existsByLeaveTypeId(Integer leaveTypeId);
    @Query("SELECT lr FROM LeaveRequest lr " +
            "JOIN FETCH lr.employee ep " +
            "JOIN FETCH lr.leaveType lt " +
            "WHERE lr.status = :status AND ep.manager.id = :managerId")
    List<LeaveRequest> findByStatusAndManagerId(
            @Param("status") LeaveStatus status,
            @Param("managerId") Integer managerId
    );

    @Query("SELECT COUNT(l) > 0 FROM LeaveRequest l WHERE l.employee.id = :empId AND l.status = 'APPROVED' AND :targetDate BETWEEN l.startDate AND l.endDate")
    boolean hasApprovedLeaveOnDate(@Param("empId") Integer empId, @Param("targetDate") LocalDate targetDate);

    // [NEW] Fetch all leave requests for a specific employee in chronological order (newest first)
    List<LeaveRequest> findByEmployeeIdOrderByStartDateDesc(Integer employeeId);
}
