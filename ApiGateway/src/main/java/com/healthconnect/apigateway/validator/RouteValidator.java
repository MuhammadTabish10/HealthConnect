package com.healthconnect.apigateway.validator;

import com.healthconnect.apigateway.constant.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final Logger logger = LoggerFactory.getLogger(RouteValidator.class);

    private static final List<String> AUTH_WHITELIST = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/eureka",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    public final Predicate<ServerHttpRequest> isSecured =
            request -> {
                boolean secured = AUTH_WHITELIST.stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));
                logger.debug(String.format(MessageConstants.REQUEST_URI_SECURED_LOG, request.getURI(), secured));
                return secured;
            };
}
