package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.SalaryComponentResponse;
import com.cts.rivio.entity.SalaryComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalaryComponentMapper {

    @Mapping(source = "employeeProfile.id", target = "employeeProfileId")
    // MapStruct will automatically fetch the name from the linked Employee entity
    @Mapping(target = "employeeName", expression = "java(entity.getEmployeeProfile().getFirstName() + \" \" + entity.getEmployeeProfile().getLastName())")
    SalaryComponentResponse toResponse(SalaryComponent entity);
}