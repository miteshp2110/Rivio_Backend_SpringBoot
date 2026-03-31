package com.cts.rivio.modules.auth.repository;

import com.cts.rivio.modules.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Custom method to check for uniqueness
    boolean existsByName(String name);
}