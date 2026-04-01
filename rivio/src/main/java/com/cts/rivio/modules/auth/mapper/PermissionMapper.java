package com.cts.rivio.modules.auth.mapper;

import com.cts.rivio.modules.auth.dto.response.PermissionResponse;
import com.cts.rivio.modules.auth.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toResponse(Permission permission);
}