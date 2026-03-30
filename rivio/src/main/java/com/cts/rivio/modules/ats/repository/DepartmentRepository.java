// DepartmentRepository.java
package com.cts.rivio.modules.ats.repository;
import com.cts.rivio.modules.company.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {}