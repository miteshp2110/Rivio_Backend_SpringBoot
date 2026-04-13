package com.cts.rivio.service;

import com.cts.rivio.dto.request.RoleRequest;
import com.cts.rivio.dto.response.PermissionResponse;
import com.cts.rivio.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    RoleResponse getById(Integer id);
    RoleResponse update(Integer id,RoleRequest request);
    List<PermissionResponse> getPermissionsForRole(Integer roleId);
    void bindPermissionsToRole(Integer roleId, List<Integer> permissionIds);
}