package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.WorkDayResponse;
import com.cts.rivio.entity.WorkDay;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkDayMapper {
    WorkDayResponse toResponse(WorkDay workDay);
}