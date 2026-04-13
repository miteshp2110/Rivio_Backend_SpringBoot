package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.UserResponse;
import com.cts.rivio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Tell MapStruct to get the 'name' from the linked 'Role' entity
    // and map it to 'roleName' in the DTO
    @Mapping(source = "role.name", target = "roleName")
    UserResponse toResponse(User user);
}