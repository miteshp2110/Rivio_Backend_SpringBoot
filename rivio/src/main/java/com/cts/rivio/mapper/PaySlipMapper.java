package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.PaySlipResponse;
import com.cts.rivio.entity.PaySlip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaySlipMapper {
    @Mapping(target = "employeeName", expression = "java(entity.getEmployeeProfile().getFirstName() + ' ' + entity.getEmployeeProfile().getLastName())")
    @Mapping(source = "employeeProfile.id", target = "employeeProfileId")
    @Mapping(source = "employeeProfile.employeeCode", target = "employeeCode")
    @Mapping(source = "payCycle.id", target = "payCycleId")
    PaySlipResponse toResponse(PaySlip entity);
}