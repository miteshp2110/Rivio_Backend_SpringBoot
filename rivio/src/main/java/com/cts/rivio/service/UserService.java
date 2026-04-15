package com.cts.rivio.service;

import com.cts.rivio.core.common.dto.PageResponse;
import com.cts.rivio.dto.request.PasswordResetRequest;
import com.cts.rivio.dto.request.UserCreateRequest;
import com.cts.rivio.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    void resetPassword(Integer id, PasswordResetRequest request);
    String deleteUser(Integer id);
    PageResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir);
}