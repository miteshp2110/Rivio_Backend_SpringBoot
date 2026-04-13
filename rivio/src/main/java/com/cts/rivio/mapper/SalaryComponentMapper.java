package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.SalaryComponentResponse;
import com.cts.rivio.entity.SalaryComponent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryComponentMapper {
    SalaryComponentResponse toResponse(SalaryComponent entity);
}