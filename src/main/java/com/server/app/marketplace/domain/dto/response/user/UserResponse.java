package com.server.app.marketplace.domain.dto.response.user;

import com.server.app.marketplace.common.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private UserRole role;

    private Boolean active;

    private LocalDateTime createdAt;
}