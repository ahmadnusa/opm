package com.dansmultipro.ops.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "Full name is required.")
        String fullName,
        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid.")
        String email,
        @NotBlank(message = "Password is required.")
        String password) {
}
