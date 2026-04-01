package com.cts.rivio.modules.company.mapper;

import com.cts.rivio.modules.company.dto.response.DesignationResponse;
import com.cts.rivio.modules.company.entity.Designation;
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