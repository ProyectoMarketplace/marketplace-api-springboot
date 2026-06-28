package com.server.app.marketplace.domain.dto.response.user;

import com.server.app.marketplace.common.enums.UserRole;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long id;

    private String fullName;

    private String email;

    private UserRole role;

    private String token;

    private String message;
}