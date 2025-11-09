package com.dansmultipro.ops.util;

import com.dansmultipro.ops.constant.RoleType;
import com.dansmultipro.ops.pojo.AuthorizationPOJO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class AuthUtil {

    private static final UUID SYSTEM_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public UUID idSystem() {
        return resolvePrincipal()
                .map(AuthorizationPOJO::getId)
                .map(UUID::fromString)
                .orElse(SYSTEM_USER_ID);
    }

    public UUID idLogin() {
        return resolvePrincipal()
                .map(AuthorizationPOJO::getId)
                .map(UUID::fromString)
                .orElseThrow(() -> new IllegalStateException("Authentication is required."));
    }

    public RoleType roleLogin() {
        return resolvePrincipal()
                .map(AuthorizationPOJO::getRole)
                .orElseThrow(() -> new IllegalStateException("Authentication is required."));
    }

    public boolean hasRole(RoleType role) {
        return resolvePrincipal()
                .map(AuthorizationPOJO::getRole)
                .map(role::equals)
                .orElse(false);
    }

    public boolean isAuthenticated() {
        return resolvePrincipal().isPresent();
    }

    private Optional<AuthorizationPOJO> resolvePrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthorizationPOJO authorizationPOJO) {
            return Optional.of(authorizationPOJO);
        }
        return Optional.empty();
    }
}
