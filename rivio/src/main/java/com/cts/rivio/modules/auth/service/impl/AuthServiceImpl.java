package com.cts.rivio.modules.auth.service.impl;


import com.cts.rivio.core.config.JwtUtil;
import com.cts.rivio.core.exception.UnauthorizedException;
import com.cts.rivio.modules.auth.dto.request.LoginRequest;
import com.cts.rivio.modules.auth.dto.response.AuthResponse;
import com.cts.rivio.modules.auth.entity.RolePermission;
import com.cts.rivio.modules.auth.entity.User;
import com.cts.rivio.modules.auth.enums.UserStatus;
import com.cts.rivio.modules.auth.repository.RolePermissionRepository;
import com.cts.rivio.modules.auth.repository.UserRepository;
import com.cts.rivio.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Find User by Email (AC 3: 401 on invalid credentials)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // 2. Check if Suspended (AC 4: Block if user status is 'Suspended')
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new UnauthorizedException("Your account has been suspended. Please contact HR.");
        }

        // 3. Verify Password Hash (AC 1: Passwords must be verified)
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid password");
        }

        // 4. Fetch Role Permissions
        List<String> permissions = rolePermissionRepository.findByRoleId(user.getRole().getId())
                .stream()
                .map(rp -> rp.getPermission().getKeyName())
                .collect(Collectors.toList());

        String roleName = user.getRole().getName();

        // 5. Generate JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roleName, permissions);

        // 6. Return Data (AC 2: Return JWT and Permission list)
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .role(roleName)
                .permissions(permissions)
                .build();
    }
}