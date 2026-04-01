package com.cts.rivio.modules.leave.mapper;

import com.cts.rivio.modules.leave.dto.response.LeaveRequestResponse;
import com.cts.rivio.modules.leave.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    @Mapping(target = "employeeName", expression = "java(request.getEmployee().getFirstName() + ' ' + request.getEmployee().getLastName())")
    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    LeaveRequestResponse toResponse(LeaveRequest request);
}