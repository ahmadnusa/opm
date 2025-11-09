package com.dansmultipro.ops.dto.user;

public record UserResponseDto(
        String id,
        String fullName,
        String email,
        String role,
        Boolean isActive,
        Integer optLock) {
}
