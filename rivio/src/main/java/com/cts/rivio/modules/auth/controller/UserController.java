package com.cts.rivio.modules.auth.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.modules.auth.dto.request.UserCreateRequest;
import com.cts.rivio.modules.auth.dto.response.UserResponse;
import com.cts.rivio.modules.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse responseData = userService.create(request);
        return new ResponseEntity<>(ApiResponse.success(responseData, "User created successfully"), HttpStatus.CREATED);
    }
}