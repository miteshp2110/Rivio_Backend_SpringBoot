package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.DesignationResponse;
import com.cts.rivio.entity.Designation;
import org.springframework.stereotype.Component;

@Component
public class DesignationMapper {
    public DesignationResponse toResponse(Designation designation) {
        if (designation == null) return null;

        return DesignationResponse.builder()
                .id(designation.getId())
                .title(designation.getTitle())
                .departmentId(designation.getDepartment().getId())
                .departmentName(designation.getDepartment().getName())
                .build();
    }
}