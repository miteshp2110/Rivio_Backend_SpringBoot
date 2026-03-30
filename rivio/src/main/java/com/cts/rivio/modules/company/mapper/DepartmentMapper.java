package com.cts.rivio.modules.company.mapper;

import com.cts.rivio.modules.company.dto.response.DepartmentResponse;
import com.cts.rivio.modules.company.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponse toResponse(Department department) {
        if (department == null) return null;

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .managerUserId(department.getManager() != null ? department.getManager().getId() : null)
                .managerEmail(department.getManager() != null ? department.getManager().getEmail() : null)
                .build();
    }
}