package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.LoginRequest;
import com.server.app.marketplace.domain.dto.request.RegisterRequest;
import com.server.app.marketplace.domain.dto.response.user.LoginResponse;
import com.server.app.marketplace.domain.dto.response.user.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}