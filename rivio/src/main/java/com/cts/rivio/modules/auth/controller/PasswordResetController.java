package com.cts.rivio.modules.auth.controller;

import com.cts.rivio.modules.auth.dto.PasswordResetRequest;
import com.cts.rivio.modules.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class PasswordResetController {

    @Autowired
    private AuthService authService;

    // Acceptance Criteria: Admin must have 'RESET_PASSWORD' permission
    @PreAuthorize("hasAuthority('RESET_PASSWORD')")
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(
            @PathVariable Integer id, // Changed from Long to Integer
            @RequestBody PasswordResetRequest request) {

        authService.resetPassword(id, request);
        return ResponseEntity.ok("Password has been successfully reset by Admin.");
    }
}