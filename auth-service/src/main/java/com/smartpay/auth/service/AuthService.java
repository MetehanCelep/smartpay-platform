package com.smartpay.auth.service;

import com.smartpay.auth.dto.AuthResponse;
import com.smartpay.auth.dto.LoginRequest;
import com.smartpay.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
