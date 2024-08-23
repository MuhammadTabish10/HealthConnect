//package com.healthconnect.basesecurity.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtDecoders;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenFilter extends HttpFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
//
//    private final JwtDecoder jwtDecoder;
//
//    public JwtTokenFilter() {
//        String issuerUri = "http://localhost:8080/realms/healthconnect-realm";
//        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
//    }
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        logger.info("Starting JWT filter for request: {}", request.getRequestURI());
//
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            logger.warn("Authorization header is missing or does not start with 'Bearer'");
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
//        logger.info("Extracted JWT token: {}", token);
//
//        try {
//            Jwt jwt = jwtDecoder.decode(token);
//            logger.info("JWT token successfully decoded. Subject: {}", jwt.getSubject());
//
//            List<String> roles = extractRoles(jwt);
//            if (roles.isEmpty()) {
//                logger.warn("No roles found in JWT. Proceeding with no roles.");
//            } else {
//                logger.info("Extracted roles: {}", roles);
//            }
//
//            List<SimpleGrantedAuthority> authorities = roles.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
//
//            // Use Jwt as the principal directly
//            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            logger.info("JWT authentication successful for user: {}", jwt.getSubject());
//
//        } catch (Exception e) {
//            logger.error("Failed to decode or authenticate JWT token", e);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private List<String> extractRoles(Jwt jwt) {
//        // Log the entire realm_access claim for debugging
//        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
//        logger.info("realm_access claim: {}", realmAccess);
//
//        // Extract roles from realm_access.roles
//        List<String> roles = null;
//        if (realmAccess != null && realmAccess.containsKey("roles")) {
//            roles = (List<String>) realmAccess.get("roles");
//        }
//
//        if (roles == null || roles.isEmpty()) {
//            logger.warn("No roles found in realm_access. Checking resource_access...");
//
//            // Optionally, check roles in resource_access if needed
//            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
//            if (resourceAccess != null) {
//                for (Object resource : resourceAccess.values()) {
//                    Map<String, Object> resourceMap = (Map<String, Object>) resource;
//                    List<String> resourceRoles = (List<String>) resourceMap.get("roles");
//                    if (resourceRoles != null) {
//                        logger.info("Roles from resource_access: {}", resourceRoles);
//                        roles = resourceRoles.stream()
//                                .map(role -> "ROLE_" + role.toUpperCase())
//                                .collect(Collectors.toList());
//                        break; // If roles are found in one resource, use them
//                    }
//                }
//            }
//        } else {
//            logger.info("Roles from realm_access: {}", roles);
//            roles = roles.stream()
//                    .map(role -> "ROLE_" + role.toUpperCase())
//                    .collect(Collectors.toList());
//        }
//
//        return roles != null ? roles : List.of();
//    }
//
//}


package com.healthconnect.basesecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(1)
@Component
public class JwtTokenFilter extends HttpFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtDecoder jwtDecoder;

    public JwtTokenFilter() {
        String issuerUri = "http://localhost:8080/realms/healthconnect-realm";
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.info("Starting JWT filter for request: {}", request.getRequestURI());

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or does not start with 'Bearer'");
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        logger.info("Extracted JWT token: {}", token);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            logger.info("JWT token successfully decoded. Subject: {}", jwt.getSubject());

            List<String> roles = extractRoles(jwt);
            if (roles.isEmpty()) {
                logger.warn("No roles found in JWT. Proceeding with no roles.");
            } else {
                logger.info("Extracted roles: {}", roles);
            }

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Use Jwt as the principal directly
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("JWT authentication successful for user: {} (Email: {})", jwt.getSubject(), jwt.getClaimAsString("email"));

        } catch (Exception e) {
            logger.error("Failed to decode or authenticate JWT token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Log the current active user
        Authentication activeUser = SecurityContextHolder.getContext().getAuthentication();
        if (activeUser != null) {
            logger.info("Current active user: {}", activeUser.getName());
            logger.info("Authorities: {}", activeUser.getAuthorities());
        } else {
            logger.warn("No active user found in the security context.");
        }

        chain.doFilter(request, response);
    }

    private List<String> extractRoles(Jwt jwt) {
        // Log the entire realm_access claim for debugging
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        logger.info("realm_access claim: {}", realmAccess);

        // Extract roles from realm_access.roles
        List<String> roles = null;
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles = (List<String>) realmAccess.get("roles");
        }

        if (roles == null || roles.isEmpty()) {
            logger.warn("No roles found in realm_access. Checking resource_access...");

            // Optionally, check roles in resource_access if needed
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            if (resourceAccess != null) {
                for (Object resource : resourceAccess.values()) {
                    Map<String, Object> resourceMap = (Map<String, Object>) resource;
                    List<String> resourceRoles = (List<String>) resourceMap.get("roles");
                    if (resourceRoles != null) {
                        logger.info("Roles from resource_access: {}", resourceRoles);
                        roles = resourceRoles.stream()
                                .map(role -> "ROLE_" + role.toUpperCase())
                                .collect(Collectors.toList());
                        break; // If roles are found in one resource, use them
                    }
                }
            }
        } else {
            logger.info("Roles from realm_access: {}", roles);
            roles = roles.stream()
                    .map(role -> "ROLE_" + role.toUpperCase())
                    .collect(Collectors.toList());
        }

        return roles != null ? roles : List.of();
    }

}
