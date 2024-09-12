package com.healthconnect.basesecurity.filter;

import com.healthconnect.basesecurity.constant.SecurityConstants;
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
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(SecurityConstants.ISSUER_URI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (SecurityConstants.LOGIN_URI.equals(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        logger.debug(SecurityConstants.STARTING_JWT_FILTER_LOG, requestUri);
        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            logger.warn(SecurityConstants.AUTH_HEADER_MISSING_LOG);
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(SecurityConstants.BEARER_PREFIX.length()); // Remove "Bearer " prefix
        logger.debug(SecurityConstants.JWT_TOKEN_EXTRACTED_LOG, token);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            logger.debug(SecurityConstants.JWT_TOKEN_DECODED_LOG, jwt.getSubject());

            List<String> roles = extractRoles(jwt);
            if (roles.isEmpty()) {
                logger.warn(SecurityConstants.JWT_NO_ROLES_FOUND_LOG);
            } else {
                logger.debug(SecurityConstants.JWT_ROLES_EXTRACTED_LOG, roles);
            }

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Use Jwt as the principal directly
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug(SecurityConstants.JWT_AUTH_SUCCESS_LOG, jwt.getSubject(), jwt.getClaimAsString("email"));

        } catch (Exception e) {
            logger.error(SecurityConstants.JWT_AUTH_FAILED_LOG, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Log the current active user
        Authentication activeUser = SecurityContextHolder.getContext().getAuthentication();
        if (activeUser != null) {
            logger.debug(SecurityConstants.ACTIVE_USER_LOG, activeUser.getName());
            logger.debug(SecurityConstants.ACTIVE_USER_AUTHORITIES_LOG, activeUser.getAuthorities());
        } else {
            logger.warn(SecurityConstants.NO_ACTIVE_USER_LOG);
        }

        chain.doFilter(request, response);
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap(SecurityConstants.REALM_ACCESS);
        logger.debug(SecurityConstants.REALM_ACCESS_LOG, realmAccess);

        List<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.get(SecurityConstants.ROLES) instanceof List<?>) {
            roles = ((List<?>) realmAccess.get(SecurityConstants.ROLES)).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .map(role -> SecurityConstants.ROLE_PREFIX + role.toUpperCase())
                    .collect(Collectors.toList());
        }

        if (roles.isEmpty()) {
            logger.warn(SecurityConstants.NO_ROLES_REALM_ACCESS_LOG);
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(SecurityConstants.RESOURCE_ACCESS);

            if (resourceAccess != null) {
                for (Object resource : resourceAccess.values()) {
                    if (resource instanceof Map<?, ?> resourceMap) {
                        Object rolesObj = resourceMap.get(SecurityConstants.ROLES);
                        if (rolesObj instanceof List<?>) {
                            List<String> resourceRoles = ((List<?>) rolesObj).stream()
                                    .filter(role -> role instanceof String)
                                    .map(role -> (String) role)
                                    .map(role -> SecurityConstants.ROLE_PREFIX + role.toUpperCase())
                                    .toList();
                            roles.addAll(resourceRoles);
                        }
                    }
                }
            }
        }

        logger.debug(SecurityConstants.ROLES_EXTRACTED_LOG, roles);
        return roles.isEmpty() ? Collections.emptyList() : roles;
    }
}
