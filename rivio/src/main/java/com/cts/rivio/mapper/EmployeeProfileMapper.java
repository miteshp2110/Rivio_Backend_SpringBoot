package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.EmployeeDirectoryResponse;
import com.cts.rivio.dto.response.EmployeeProfileResponse;
import com.cts.rivio.entity.EmployeeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper {

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "designation.title", target = "designationTitle")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(target = "managerName", expression = "java(profile.getManager() != null ? profile.getManager().getFirstName() + ' ' + profile.getManager().getLastName() : null)")
    EmployeeProfileResponse toResponse(EmployeeProfile profile);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "designation.title", target = "designationTitle")
    EmployeeDirectoryResponse toDirectoryResponse(EmployeeProfile profile);
}