package com.dansmultipro.ops.dto.auth;

import java.time.Instant;

public record TokenPair(String token, Instant expiresAt) {
}
