package com.cts.rivio.modules.auth.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.auth.dto.request.RoleRequest;
import com.cts.rivio.modules.auth.dto.response.RoleResponse;
import com.cts.rivio.modules.auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        RoleResponse responseData = service.create(request);

        // AC 2: Returns HTTP 201 Created using our standard ApiResponse wrapper
        return new ResponseEntity<>(
                ApiResponse.success(responseData, "Role created successfully"),
                HttpStatus.CREATED
        );
    }
}