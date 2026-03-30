package com.cts.rivio.modules.auth.service.impl;

import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.modules.auth.dto.request.PasswordResetRequest;
import com.cts.rivio.modules.auth.dto.request.UserCreateRequest;
import com.cts.rivio.modules.auth.dto.response.UserResponse;
import com.cts.rivio.modules.auth.entity.Role;
import com.cts.rivio.modules.auth.entity.User;
import com.cts.rivio.modules.auth.enums.UserStatus;
import com.cts.rivio.modules.auth.mapper.UserMapper;
import com.cts.rivio.modules.auth.repository.RoleRepository;
import com.cts.rivio.modules.auth.repository.UserRepository;
import com.cts.rivio.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(UserCreateRequest request) {

        // AC 1: Email must be unique
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use.");
        }

        // Validate that the assigned Role actually exists
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(role);

        // AC 2: Password must be securely hashed
        String hashed = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashed);

        // AC 3: Default status is 'Active'
        user.setStatus(UserStatus.Active);

        // Save and Map
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void resetPassword(Integer id, PasswordResetRequest request) {
        // Fetch the user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // AC 2: New password must be securely hashed
        String hashed = passwordEncoder.encode(request.getNewPassword());
        user.setPasswordHash(hashed);

        // Save the updated user
        userRepository.save(user);
    }
}