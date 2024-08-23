package com.healthconnect.basesecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(2)
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtDecoder jwtDecoder;

    public JwtTokenFilter() {
        String issuerUri = "http://localhost:8080/realms/healthconnect-realm";
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // Bypass the filter for /api/v1/users/login
        if ("/api/v1/users/login".equals(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        logger.debug("Starting JWT filter for request: {}", requestUri);
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or does not start with 'Bearer'");
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        logger.debug("Extracted JWT token: {}", token);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            logger.debug("JWT token successfully decoded. Subject: {}", jwt.getSubject());

            List<String> roles = extractRoles(jwt);
            if (roles.isEmpty()) {
                logger.warn("No roles found in JWT. Proceeding with no roles.");
            } else {
                logger.debug("Extracted roles: {}", roles);
            }

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Use Jwt as the principal directly
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("JWT authentication successful for user: {} (Email: {})", jwt.getSubject(), jwt.getClaimAsString("email"));

        } catch (Exception e) {
            logger.error("Failed to decode or authenticate JWT token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Log the current active user
        Authentication activeUser = SecurityContextHolder.getContext().getAuthentication();
        if (activeUser != null) {
            logger.debug("Current active user: {}", activeUser.getName());
            logger.debug("Authorities: {}", activeUser.getAuthorities());
        } else {
            logger.warn("No active user found in the security context.");
        }

        chain.doFilter(request, response);
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        logger.debug("realm_access claim: {}", realmAccess);

        List<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
            roles = ((List<?>) realmAccess.get("roles")).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .map(role -> "ROLE_" + role.toUpperCase())
                    .collect(Collectors.toList());
        }

        if (roles.isEmpty()) {
            logger.warn("No roles found in realm_access. Checking resource_access...");
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

            if (resourceAccess != null) {
                for (Object resource : resourceAccess.values()) {
                    if (resource instanceof Map<?, ?> resourceMap) {
                        Object rolesObj = resourceMap.get("roles");
                        if (rolesObj instanceof List<?>) {
                            List<String> resourceRoles = ((List<?>) rolesObj).stream()
                                    .filter(role -> role instanceof String)
                                    .map(role -> (String) role)
                                    .map(role -> "ROLE_" + role.toUpperCase())
                                    .toList();
                            roles.addAll(resourceRoles);
                        }
                    }
                }
            }
        }

        logger.debug("Roles extracted from JWT: {}", roles);
        return roles.isEmpty() ? Collections.emptyList() : roles;
    }
}
