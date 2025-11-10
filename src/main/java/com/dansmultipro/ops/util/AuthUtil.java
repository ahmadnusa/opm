package com.dansmultipro.ops.util;

import com.dansmultipro.ops.constant.RoleTypeConstant;
import com.dansmultipro.ops.pojo.AuthorizationPOJO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class AuthUtil {

    public boolean isAuthenticated() {
        return resolvePrincipal().isPresent();
    }

    public UUID getLoginId() {
        AuthorizationPOJO principal = resolvePrincipal()
                .orElseThrow(() -> new IllegalStateException("Authentication is required."));
        return UUID.fromString(principal.getId());
    }

    public RoleTypeConstant roleLogin() {
        AuthorizationPOJO principal = resolvePrincipal()
                .orElseThrow(() -> new IllegalStateException("Authentication is required."));
        return principal.getRole();
    }

    public boolean hasRole(RoleTypeConstant role) {
        Optional<AuthorizationPOJO> principal = resolvePrincipal();
        return principal.map(authorizationPOJO -> authorizationPOJO.getRole().equals(role)).orElse(false);
    }

    private Optional<AuthorizationPOJO> resolvePrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthorizationPOJO authorizationPOJO) {
                return Optional.of(authorizationPOJO);
            }
        }
        return Optional.empty();
    }
}
