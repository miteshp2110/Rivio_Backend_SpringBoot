package com.cts.rivio.modules.auth.service;

import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);
}