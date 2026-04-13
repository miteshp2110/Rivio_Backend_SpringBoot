package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    @Mapping(target = "employeeName", expression = "java(request.getEmployee().getFirstName() + ' ' + request.getEmployee().getLastName())")
    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    LeaveRequestResponse toResponse(LeaveRequest request);
}