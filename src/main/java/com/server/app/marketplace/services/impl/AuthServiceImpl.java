package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.mappers.UserMapper;
import com.server.app.marketplace.domain.dto.request.LoginRequest;
import com.server.app.marketplace.domain.dto.request.RegisterRequest;
import com.server.app.marketplace.domain.dto.response.user.LoginResponse;
import com.server.app.marketplace.domain.dto.response.user.UserResponse;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessRuleException("Email is already registered.");
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessRuleException("Invalid credentials.");
        }

        if (!user.getActive()) {
            throw new BusinessRuleException("User account is inactive.");
        }

        return userMapper.toLoginDto(user);
    }
}