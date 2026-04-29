package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.dto.response.EmployeeProfileResponse;
import com.cts.rivio.entity.EmployeeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// FIX: Add the "uses" parameter here so MapStruct knows how to map the new salary list!
@Mapper(componentModel = "spring", uses = {SalaryComponentMapper.class})
public interface EmployeeProfileMapper {

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "designation.title", target = "designationTitle")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(target = "managerName", expression = "java(profile.getManager() != null ? profile.getManager().getFirstName() + ' ' + profile.getManager().getLastName() : null)")
    EmployeeProfileResponse toResponse(EmployeeProfile profile);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "designation.title", target = "designationTitle")
    @Mapping(source = "user.email", target = "email") // Pulls email from the User entity

    // NEW: Tell MapStruct to dig into the User -> Role -> Name
    @Mapping(source = "user.role.name", target = "roleName")

    EmployeeDirectoryResponse toDirectoryResponse(EmployeeProfile entity);
}