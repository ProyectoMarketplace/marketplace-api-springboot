package com.server.app.marketplace.domain.dto.request;

import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.validations.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email format is invalid.")
    @UniqueUserEmail
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotNull(message = "Role is required.")
    private UserRole role;
}