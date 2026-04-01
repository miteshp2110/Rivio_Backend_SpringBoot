package com.cts.rivio.modules.auth.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.PermissionResponse;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;
import com.cts.rivio.modules.auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        RoleResponse responseData = service.create(request);
        return new ResponseEntity<>(ApiResponse.success(responseData, "Role created successfully"), HttpStatus.CREATED);
    }

    // --- NEW ENDPOINTS ---

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        List<RoleResponse> roles = service.getAll();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable Integer id) {
        RoleResponse role = service.getById(id);
        return ResponseEntity.ok(ApiResponse.success(role, "Role fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(@PathVariable Integer id, @Valid @RequestBody RoleRequest request) {
        RoleResponse updatedRole = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedRole, "Role updated successfully"));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getRolePermissions(@PathVariable Integer id) {
        List<PermissionResponse> permissions = service.getPermissionsForRole(id);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Role permissions fetched successfully"));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<Void>> bindPermissions(
            @PathVariable Integer id,
            @RequestBody List<Integer> permissionIds) { // Accepts a raw JSON array of integers: [1, 2, 5]

        service.bindPermissionsToRole(id, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Permissions bound to role successfully"));
    }
}