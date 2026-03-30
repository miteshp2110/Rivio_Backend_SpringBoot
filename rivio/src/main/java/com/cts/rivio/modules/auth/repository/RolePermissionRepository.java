package com.cts.rivio.modules.auth.repository;

import com.cts.rivio.modules.auth.entity.RolePermission;
import com.cts.rivio.modules.auth.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    List<RolePermission> findByRoleId(Integer roleId);
    void deleteByRoleId(Integer roleId);
}