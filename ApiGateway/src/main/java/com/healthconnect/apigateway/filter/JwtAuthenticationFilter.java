package com.healthconnect.apigateway.filter;

import com.healthconnect.apigateway.constant.MessageConstants;
import com.healthconnect.apigateway.util.JwtUtils;
import com.healthconnect.apigateway.validator.RouteValidator;
import com.healthconnect.baseservice.exception.JwtTokenMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String EMAIL = "email";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final RouteValidator routeValidator;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, RouteValidator routeValidator) {
        this.jwtUtils = jwtUtils;
        this.routeValidator = routeValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().toString();
        logger.info(String.format(MessageConstants.PROCESSING_REQUEST_LOG, requestUri));

        return routeValidator.isSecured.test(exchange.getRequest())
                ? extractToken(exchange)
                .flatMap(token -> validateToken(exchange, chain, token))
                : chain.filter(exchange);
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> {
                    boolean valid = authHeader != null && authHeader.startsWith(BEARER_PREFIX);
                    if (!valid) {
                        logger.warn(MessageConstants.AUTH_HEADER_MISSING_OR_INVALID);
                    }
                    return valid;
                })
                .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()).trim())
                .doOnNext(token -> logger.debug(String.format(MessageConstants.EXTRACTED_JWT_TOKEN_LOG, token)))
                .switchIfEmpty(Mono.error(new JwtTokenMissingException(MessageConstants.JWT_MISSING_ERROR)));
    }

    private Mono<Void> validateToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        try {
            if (jwtUtils.isTokenValid(token)) {
                String email = jwtUtils.extractUsername(token);
                logger.info(String.format(MessageConstants.JWT_VALID_LOG, email));
                exchange.getRequest().mutate().header(EMAIL, email).build();
                return chain.filter(exchange);
            } else {
                logger.warn(MessageConstants.JWT_INVALID_LOG);
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, MessageConstants.JWT_INVALID_LOG));
            }
        } catch (Exception e) {
            logger.error(String.format(MessageConstants.JWT_VALIDATION_EXCEPTION_LOG, e.getMessage()));
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, MessageConstants.JWT_INVALID_LOG));
        }
    }
}
