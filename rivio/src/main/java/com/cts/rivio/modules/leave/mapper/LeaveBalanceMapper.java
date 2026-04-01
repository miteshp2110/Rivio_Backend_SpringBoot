package com.cts.rivio.modules.leave.mapper;

import com.cts.rivio.modules.leave.dto.response.LeaveBalanceResponse;
import com.cts.rivio.modules.leave.entity.EmployeeLeaveBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveBalanceMapper {

    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    LeaveBalanceResponse toResponse(EmployeeLeaveBalance balance);
}