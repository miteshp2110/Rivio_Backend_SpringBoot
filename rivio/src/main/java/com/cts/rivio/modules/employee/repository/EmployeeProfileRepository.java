package com.cts.rivio.modules.employee.repository; // This must match exactly

import com.cts.rivio.modules.employee.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    // AC 1: Needed to find the employee by their ID string from the CSV
    Optional<EmployeeProfile> findByEmployeeId(String employeeId);
}