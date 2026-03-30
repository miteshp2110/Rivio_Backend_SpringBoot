package com.cts.rivio.modules.auth.service;

import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    RoleResponse getById(Integer id);
    RoleResponse update(Integer id,RoleRequest request);
}