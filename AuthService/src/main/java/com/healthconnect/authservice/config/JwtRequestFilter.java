package com.healthconnect.authservice.config;

import com.healthconnect.authservice.service.JwtService;
import com.healthconnect.authservice.service.impl.CustomUserDetailServiceImpl;
import com.healthconnect.baseservice.exception.JwtTokenInvalidException;
import com.healthconnect.baseservice.exception.JwtTokenMissingException;
import com.healthconnect.baseservice.exception.UserNotFoundException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final JwtService jwtUtil;
    private final CustomUserDetailServiceImpl customUserDetailService;

    private static final List<String> AUTH_WHITELIST = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/swagger-ui.html",
            "/v2/api-docs"
    );

    @Autowired
    public JwtRequestFilter(JwtService jwtUtil, CustomUserDetailServiceImpl customUserDetailService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        // Skip the filter for whitelisted paths
        if (isWhitelisted(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
                throw new JwtTokenMissingException("JWT Token is missing or does not begin with Bearer");
            }

            String jwt = authorizationHeader.substring(BEARER_PREFIX.length());
            String username = jwtUtil.extractUsername(jwt);

            if (username == null) {
                throw new JwtTokenInvalidException("JWT Token is invalid");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetail customUserDetail = customUserDetailService.loadUserByUsername(username);

                if (customUserDetail == null) {
                    throw new UserNotFoundException("User not found with email: " + username);
                }

                if (jwtUtil.isTokenValid(jwt, customUserDetail)) {
                    setAuthentication(request, customUserDetail);
                } else {
                    throw new JwtTokenInvalidException("JWT Token is invalid");
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtTokenMissingException | JwtTokenInvalidException | UserNotFoundException e) {
            logger.error("Error processing JWT token: {}", e.getMessage());
            handleException(response, e);
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            handleException(response, e);
        }
    }

    private boolean isWhitelisted(String path) {
        return AUTH_WHITELIST.stream().anyMatch(path::startsWith);
    }

    private void setAuthentication(HttpServletRequest request, CustomUserDetail customUserDetail) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUserDetail, null, customUserDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
