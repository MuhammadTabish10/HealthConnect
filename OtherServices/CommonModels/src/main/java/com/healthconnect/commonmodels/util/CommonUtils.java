package com.healthconnect.commonmodels.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {
    private static final String SYSTEM = "system";
    private static final String EMAIL = "email";

    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return SYSTEM;
        }

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString(EMAIL);
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}
