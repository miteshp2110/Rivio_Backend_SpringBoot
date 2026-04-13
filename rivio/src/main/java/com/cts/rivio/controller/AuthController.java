package com.cts.rivio.controller;


import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.dto.request.LoginRequest;
import com.cts.rivio.dto.response.AuthResponse;
import com.cts.rivio.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse responseData = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(responseData, "Login successful"));
    }
}