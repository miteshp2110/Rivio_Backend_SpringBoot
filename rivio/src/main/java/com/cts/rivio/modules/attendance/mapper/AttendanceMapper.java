package com.cts.rivio.modules.attendance.mapper;

import com.cts.rivio.modules.attendance.dto.response.AttendanceResponse;
import com.cts.rivio.modules.attendance.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "employeeName", expression = "java(attendance.getEmployeeProfile().getFirstName() + ' ' + attendance.getEmployeeProfile().getLastName())")
    AttendanceResponse toResponse(Attendance attendance);
}