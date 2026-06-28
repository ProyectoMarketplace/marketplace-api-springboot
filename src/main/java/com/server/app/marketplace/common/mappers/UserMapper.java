package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.RegisterRequest;
import com.server.app.marketplace.domain.dto.response.user.LoginResponse;
import com.server.app.marketplace.domain.dto.response.user.UserResponse;
import com.server.app.marketplace.domain.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public LoginResponse toLoginDto(User user, String token) {
        return LoginResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .message("Login successful")
                .build();
    }
}