package com.cts.rivio.modules.attendance.mapper;

import com.cts.rivio.modules.attendance.dto.response.WorkDayResponse;
import com.cts.rivio.modules.attendance.entity.WorkDay;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkDayMapper {
    WorkDayResponse toResponse(WorkDay workDay);
}