package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.response.PermissionResponse;
import com.cts.rivio.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllPermissions(), "Permissions fetched successfully"));
    }
}