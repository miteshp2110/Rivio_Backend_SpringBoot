package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.LeaveRequestResponse;
import com.cts.rivio.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    // Map the IDs
    @Mapping(source = "employee.id", target = "employeeProfileId")
    @Mapping(source = "leaveType.id", target = "leaveTypeId")

    // Your custom display name mappings
    @Mapping(target = "employeeName", expression = "java(request.getEmployee().getFirstName() + ' ' + request.getEmployee().getLastName())")
    @Mapping(source = "leaveType.name", target = "leaveTypeName")

    // [NEW] Map the approvedBy entity's ID for the HR Admin / Manager Approval features
    @Mapping(source = "approvedBy.id", target = "approvedByProfileId")
    LeaveRequestResponse toResponse(LeaveRequest request);
}