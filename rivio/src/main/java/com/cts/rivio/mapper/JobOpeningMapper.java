package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.JobOpeningResponse;
import com.cts.rivio.entity.JobOpening;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobOpeningMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    JobOpeningResponse toResponse(JobOpening entity);
}