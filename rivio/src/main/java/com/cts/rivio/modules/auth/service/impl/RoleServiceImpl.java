package com.cts.rivio.modules.auth.service.impl;

import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;
import com.cts.rivio.modules.auth.entity.Role;
import com.cts.rivio.modules.auth.mapper.RoleMapper;
import com.cts.rivio.modules.auth.repository.RoleRepository;
import com.cts.rivio.modules.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public RoleResponse create(RoleRequest request) {

        // AC 1: Enforce Unique Constraint
        if (repository.existsByName(request.getName())) {
            // This throws an exception that will be caught by our GlobalExceptionHandler
            // and automatically returned to the user as a 400 Bad Request
            throw new IllegalArgumentException("A role with the name '" + request.getName() + "' already exists.");
        }

        // Map DTO to Entity
        Role role = mapper.toEntity(request);

        // Save to Database
        role = repository.save(role);

        // Map saved Entity back to Response DTO
        return mapper.toResponse(role);
    }
}