package com.cts.rivio.modules.leave.repository;

import com.cts.rivio.modules.leave.entity.LeaveRequest;
import com.cts.rivio.modules.leave.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    // This query finds requests where the employee's manager ID matches the input
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.manager.id = :managerId AND lr.status = :status")
    List<LeaveRequest> findByManagerIdAndStatus(@Param("managerId") Integer managerId, @Param("status") LeaveStatus status);
}
