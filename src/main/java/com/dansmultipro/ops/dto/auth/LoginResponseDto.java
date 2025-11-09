package com.dansmultipro.ops.dto.auth;

public record LoginResponseDto(
  String fullName,
  String role,
  String accessToken, 
  String expiresAt) {
}
