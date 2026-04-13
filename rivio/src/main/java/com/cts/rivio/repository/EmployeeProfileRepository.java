package com.cts.rivio.repository;

import com.cts.rivio.entity.EmployeeProfile;
import com.cts.rivio.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}