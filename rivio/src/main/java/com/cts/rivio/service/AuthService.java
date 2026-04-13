package com.cts.rivio.service;


import com.cts.rivio.dto.request.LoginRequest;
import com.cts.rivio.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}