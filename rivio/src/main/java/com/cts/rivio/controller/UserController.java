package com.cts.rivio.controller;

import com.cts.rivio.core.common.dto.ApiResponse;
import com.cts.rivio.core.common.dto.PageResponse;
import com.cts.rivio.dto.request.PasswordResetRequest;
import com.cts.rivio.dto.request.UserCreateRequest;
import com.cts.rivio.dto.response.UserResponse;
import com.cts.rivio.service.UserService;
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

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordResetRequest request) {

        userService.resetPassword(id, request);

        // Return a clean success message with no data payload
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully. Notification sent to user."));
    }
    /**
     * Get Paginated List of Users
     * Example URL: GET /api/users?page=0&size=10&sortBy=email&sortDir=asc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageResponse<UserResponse> response = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response, "Users fetched successfully"));
    }
    /**
     * Delete a User (With Auto-Suspend Fallback)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {

        String resultMessage = userService.deleteUser(id);

        // We pass the dynamic resultMessage to the ApiResponse so the UI shows exactly what happened
        return ResponseEntity.ok(ApiResponse.success(null, resultMessage));
    }
}