package com.cts.rivio.modules.auth.mapper;

import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;
import com.cts.rivio.modules.auth.entity.Role;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);
    RoleResponse toResponse(Role role);
}