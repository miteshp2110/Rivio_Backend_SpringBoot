package com.cts.rivio.modules.auth.service;

import com.cts.rivio.modules.auth.dto.request.PasswordResetRequest;
import com.cts.rivio.modules.auth.dto.request.UserCreateRequest;
import com.cts.rivio.modules.auth.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    void resetPassword(Integer id, PasswordResetRequest request);
}