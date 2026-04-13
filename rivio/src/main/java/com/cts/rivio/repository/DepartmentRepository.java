package com.cts.rivio.repository;

import com.cts.rivio.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name); // Required for uniqueness check
}