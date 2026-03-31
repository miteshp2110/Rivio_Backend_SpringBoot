package com.cts.rivio.modules.auth.service;

import com.cts.rivio.modules.auth.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {
    List<PermissionResponse> getAllPermissions();
}