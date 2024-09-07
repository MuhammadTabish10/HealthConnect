package com.healthconnect.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.apigateway.util.TokenUtils;
import com.healthconnect.apigateway.validator.RouteValidator;
import com.healthconnect.baseservice.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.healthconnect.apigateway.constant.MessageConstants.*;
import static com.healthconnect.apigateway.constant.ResourceConstants.*;

@Component
public class JwtRedisTokenFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRedisTokenFilter.class);
    private final RedisService redisService;
    private final TokenUtils tokenUtils;
    private final ObjectMapper objectMapper;
    private final RouteValidator routeValidator;

    public JwtRedisTokenFilter(RedisService redisService, TokenUtils tokenUtils, ObjectMapper objectMapper, RouteValidator routeValidator) {
        this.redisService = redisService;
        this.tokenUtils = tokenUtils;
        this.objectMapper = objectMapper;
        this.routeValidator = routeValidator;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        if (!routeValidator.isSecured.test(exchange.getRequest())) {
            log.debug(PUBLIC_ROUTE_LOG);
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .filter(principal -> principal instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuthToken -> {
                    String jti = jwtAuthToken.getToken().getId();
                    long expirationTime = Objects.requireNonNull(jwtAuthToken.getToken().getExpiresAt()).getEpochSecond();
                    long currentTime = Instant.now().getEpochSecond();

                    return redisService.getTokenData(jti)
                            .flatMap(existingData -> {
                                if (!existingData.isEmpty()) {
                                    Long storedExpirationTime = (Long) existingData.get(TOKEN_EXP_KEY);
                                    if (storedExpirationTime != null && storedExpirationTime > currentTime) {
                                        log.debug(VALID_TOKEN_LOG);
                                        return chain.filter(exchange);
                                    } else {
                                        log.debug(EXPIRED_TOKEN_LOG);
                                        redisService.deleteTokenData(jti).subscribe();
                                    }
                                }

                                Map<String, Object> tokenData = new HashMap<>();
                                tokenData.put(TOKEN_USER_ID_KEY, jwtAuthToken.getToken().getSubject());
                                tokenData.put(TOKEN_EMAIL_KEY, jwtAuthToken.getToken().getClaimAsString(TOKEN_EMAIL_KEY));

                                List<String> roles = tokenUtils.extractRoles(jwtAuthToken.getToken());
                                try {
                                    String rolesJson = objectMapper.writeValueAsString(roles);
                                    tokenData.put(TOKEN_ROLES_KEY, rolesJson);
                                } catch (JsonProcessingException e) {
                                    log.error(ERROR_SERIALIZING_ROLES_LOG, e.getMessage(), e);
                                    return Mono.error(e);
                                }

                                tokenData.put(TOKEN_ISSUER_KEY, jwtAuthToken.getToken().getIssuer().toString());
                                tokenData.put(TOKEN_AZP_KEY, jwtAuthToken.getToken().getClaimAsString(TOKEN_AZP_KEY));
                                tokenData.put(TOKEN_EXP_KEY, String.valueOf(expirationTime));

                                log.debug(STORING_TOKEN_LOG, jti, tokenData);

                                return redisService.storeTokenData(jti, tokenData, expirationTime)
                                        .then(chain.filter(exchange));
                            });
                })
                .onErrorResume(e -> {
                    log.error(ERROR_REDIS_LOG, e.getMessage(), e);
                    return chain.filter(exchange);
                });
    }
}