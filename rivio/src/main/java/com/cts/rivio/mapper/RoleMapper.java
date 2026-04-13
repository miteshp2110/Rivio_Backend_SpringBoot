package com.cts.rivio.mapper;

import com.cts.rivio.dto.request.RoleRequest;
import com.cts.rivio.dto.response.RoleResponse;
import com.cts.rivio.entity.Role;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);
    RoleResponse toResponse(Role role);
}