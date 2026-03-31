package com.cts.rivio.modules.employee.repository;

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {

    // AC 1: Ensure Employee Code is unique
    boolean existsByEmployeeCode(String employeeCode);

    // Ensure the User isn't already linked to another profile
    boolean existsByUserId(Integer userId);
}