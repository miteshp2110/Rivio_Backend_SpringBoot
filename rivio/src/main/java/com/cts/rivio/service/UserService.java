package com.cts.rivio.service;

import com.cts.rivio.dto.request.PasswordResetRequest;
import com.cts.rivio.dto.request.UserCreateRequest;
import com.cts.rivio.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    void resetPassword(Integer id, PasswordResetRequest request);
}