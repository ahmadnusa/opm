package com.dansmultipro.ops.util;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dansmultipro.ops.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secretValue;

    @Value("${security.jwt.expMinutes}")
    private long expMinutes;

    private SecretKey setSecretKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretValue);
        } catch (IllegalArgumentException ex) {
            keyBytes = secretValue.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(expMinutes));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().getCode())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiresAt))
                .signWith(setSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(setSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired token");
        } catch (JwtException ex) {
            throw new JwtException("Invalid token provided");
        }
    }
}
