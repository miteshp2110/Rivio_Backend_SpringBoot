package com.cts.rivio.service;

import com.cts.rivio.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {
    List<PermissionResponse> getAllPermissions();
}