package com.cts.rivio.service.impl;

import com.cts.rivio.core.common.dto.PageResponse;
import com.cts.rivio.core.exception.ResourceNotFoundException;
import com.cts.rivio.dto.request.PasswordResetRequest;
import com.cts.rivio.dto.request.UserCreateRequest;
import com.cts.rivio.dto.response.UserResponse;
import com.cts.rivio.entity.Role;
import com.cts.rivio.entity.User;
import com.cts.rivio.enums.UserStatus;
import com.cts.rivio.mapper.UserMapper;
import com.cts.rivio.repository.EmployeeProfileRepository;
import com.cts.rivio.repository.RoleRepository;
import com.cts.rivio.repository.UserRepository;
import com.cts.rivio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmployeeProfileRepository employeeProfileRepository;

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
        user.setStatus(UserStatus.ACTIVE);

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

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir) {
        // Handle sorting direction dynamically
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create the Pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch paginated data from DB
        Page<User> usersPage = userRepository.findAll(pageable);

        // Map Entities to DTOs (Spring Data Page supports .map() natively!)
        Page<UserResponse> dtoPage = usersPage.map(userMapper::toResponse);

        // Wrap in our custom PageResponse
        return new PageResponse<>(dtoPage);
    }
    @Override
    @Transactional
    public String deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // AC 1: Safe Delete Check
        boolean isLinkedToEmployee = employeeProfileRepository.existsByUserId(id);

        if (isLinkedToEmployee ) {
            // If they are already suspended, just let the admin know
            if (user.getStatus() == UserStatus.SUSPENDED) {
                return "User is already SUSPENDED. Hard deletion is permanently blocked due to linked historical records.";
            }

            // Fallback to Soft Delete
            user.setStatus(UserStatus.SUSPENDED);
            userRepository.save(user);
            return "Hard delete blocked for data integrity. User account was automatically SUSPENDED because they are linked to an Employee Profile or Audit Logs.";
        }

        // If it's a brand new user who never did anything, safe to hard delete!
        userRepository.delete(user);
        return "User account was permanently deleted.";
    }
}