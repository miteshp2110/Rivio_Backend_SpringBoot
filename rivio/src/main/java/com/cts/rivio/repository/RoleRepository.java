package com.cts.rivio.repository;

import com.cts.rivio.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Custom method to check for uniqueness
    boolean existsByName(String name);
}