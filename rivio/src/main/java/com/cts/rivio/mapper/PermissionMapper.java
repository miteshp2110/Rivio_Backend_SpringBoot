package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.PermissionResponse;
import com.cts.rivio.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toResponse(Permission permission);
}