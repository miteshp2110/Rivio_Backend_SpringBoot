package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.AttendanceResponse;
import com.cts.rivio.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "employeeName", expression = "java(attendance.getEmployeeProfile().getFirstName() + ' ' + attendance.getEmployeeProfile().getLastName())")
    AttendanceResponse toResponse(Attendance attendance);
}