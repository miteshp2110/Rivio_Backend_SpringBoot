package com.cts.rivio.modules.employee.repository;

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {

    // You might want to only allocate leave to active employees later
    // List<EmployeeProfile> findByIsActiveTrue();
}