package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.PayCycleResponse;
import com.cts.rivio.entity.PayCycle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayCycleMapper {
    PayCycleResponse toResponse(PayCycle payCycle);
}