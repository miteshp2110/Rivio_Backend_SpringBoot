package com.cts.rivio.modules.auth.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.PermissionResponse;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;
import com.cts.rivio.modules.auth.entity.Role;
import com.cts.rivio.modules.auth.entity.RolePermission;
import com.cts.rivio.modules.auth.mapper.PermissionMapper;
import com.cts.rivio.modules.auth.mapper.RoleMapper;
import com.cts.rivio.modules.auth.repository.PermissionRepository;
import com.cts.rivio.modules.auth.repository.RolePermissionRepository;
import com.cts.rivio.modules.auth.repository.RoleRepository;
import com.cts.rivio.modules.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionMapper permissionMapper;

    // AC 3: Define which role IDs are locked and cannot be modified (e.g., 1=Admin, 2=Employee)
    private static final List<Integer> CORE_SYSTEM_ROLES = List.of(1, 2);

    @Override
    public RoleResponse create(RoleRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("A role with the name '" + request.getName() + "' already exists.");
        }
        Role role = mapper.toEntity(request);
        role = repository.save(role);
        return mapper.toResponse(role);
    }

    // --- NEW METHODS ---

    // AC 1: GET returns a list of all roles
    @Override
    public List<RoleResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getById(Integer id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return mapper.toResponse(role);
    }

    // AC 2: PUT updates the name
    @Override
    public RoleResponse update(Integer id, RoleRequest request) {
        // AC 3: Cannot modify core system roles
        if (CORE_SYSTEM_ROLES.contains(id)) {
            throw new IllegalArgumentException("Core system roles (Admin/Employee) cannot be modified.");
        }

        Role role = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // Ensure the NEW name doesn't conflict with another existing role
        if (!role.getName().equalsIgnoreCase(request.getName()) && repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("A role with the name '" + request.getName() + "' already exists.");
        }

        role.setName(request.getName());
        role = repository.save(role);

        return mapper.toResponse(role);
    }

    @Override
    public List<PermissionResponse> getPermissionsForRole(Integer roleId) {
        // Ensure role exists first
        repository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        return rolePermissionRepository.findByRoleId(roleId).stream()
                .map(RolePermission::getPermission)
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Critical: Ensures full rollback if any DB operation fails
    public void bindPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        // Delete all existing mapped permissions (AC 1: Replace existing)
        rolePermissionRepository.deleteByRoleId(roleId);

        if (permissionIds == null || permissionIds.isEmpty()) {
            return; // If they passed an empty list, we just leave it cleared
        }

        // Fetch the actual Permission entities
        List<com.cts.rivio.modules.auth.entity.Permission> permissions = permissionRepository.findAllById(permissionIds);

        // AC 2: Invalid IDs throw 400 Bad Request
        if (permissions.size() != permissionIds.size()) {
            throw new IllegalArgumentException("One or more Permission IDs provided are invalid.");
        }

        // Create new mappings and save them
        List<RolePermission> newMappings = permissions.stream()
                .map(permission -> {
                    RolePermission rp = new RolePermission();
                    rp.setRole(role);
                    rp.setPermission(permission);
                    return rp;
                })
                .collect(Collectors.toList());

        rolePermissionRepository.saveAll(newMappings);
    }
}