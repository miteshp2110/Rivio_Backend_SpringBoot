package com.cts.rivio.repository;

import com.cts.rivio.entity.SalaryComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryComponentRepository extends JpaRepository<SalaryComponent, Integer> {

    List<SalaryComponent> findByEmployeeProfileId(Integer employeeProfileId);

    // Used to prevent duplicate component names for the same employee
    boolean existsByEmployeeProfileIdAndNameIgnoreCase(Integer employeeProfileId, String name);

    boolean existsByEmployeeProfileIdAndNameIgnoreCaseAndIdNot(Integer employeeProfileId, String name, Integer id);
}