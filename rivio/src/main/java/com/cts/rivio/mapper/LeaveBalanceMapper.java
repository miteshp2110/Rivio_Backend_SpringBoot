package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.LeaveBalanceResponse;
import com.cts.rivio.entity.EmployeeLeaveBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveBalanceMapper {

    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    LeaveBalanceResponse toResponse(EmployeeLeaveBalance balance);
}