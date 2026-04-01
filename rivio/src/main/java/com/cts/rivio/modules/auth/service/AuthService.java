package com.cts.rivio.modules.auth.service;


import com.cts.rivio.modules.auth.dto.request.LoginRequest;
import com.cts.rivio.modules.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}