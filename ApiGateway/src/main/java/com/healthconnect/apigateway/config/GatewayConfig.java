package com.healthconnect.apigateway.config;

import com.healthconnect.apigateway.constant.MessageConstants;
import com.healthconnect.apigateway.constant.RouteConstants;
import com.healthconnect.apigateway.filter.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final String AUTH_SERVICE = "AUTH-SERVICE";
    private static final String USER_SERVICE = "USER-SERVICE";

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        logger.info(MessageConstants.SETTING_UP_ROUTES_LOG);
        return builder.routes()
                .route(AUTH_SERVICE, r -> r.path(RouteConstants.AUTH_SERVICE_PATH)
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri(RouteConstants.AUTH_SERVICE_URI))
                .route(USER_SERVICE, r -> r.path(RouteConstants.USER_SERVICE_PATH)
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri(RouteConstants.USER_SERVICE_URI))
                .build();
    }
}
