package com.healthconnect.apigateway.config;

import com.healthconnect.apigateway.constant.MessageConstants;
import com.healthconnect.apigateway.constant.RouteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);

    private final RateLimiterConfig rateLimiterConfig;

    public GatewayConfig(RateLimiterConfig rateLimiterConfig) {
        this.rateLimiterConfig = rateLimiterConfig;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        LOGGER.info(MessageConstants.SETTING_UP_ROUTES_LOG);
        return builder.routes()
                .route(RouteConstants.USER_SERVICE_ID, r -> r.path(RouteConstants.USER_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(rateLimiterConfig.redisRateLimiter())
                                .setKeyResolver(rateLimiterConfig.ipKeyResolver())))
                        .uri(RouteConstants.USER_SERVICE_URI))
                .route(RouteConstants.LOCATION_SERVICE_ID, r -> r.path(RouteConstants.LOCATION_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(rateLimiterConfig.redisRateLimiter())
                                .setKeyResolver(rateLimiterConfig.ipKeyResolver())))
                        .uri(RouteConstants.LOCATION_SERVICE_URI))
                .route(RouteConstants.HOSPITAL_SERVICE_ID, r -> r.path(RouteConstants.HOSPITAL_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(rateLimiterConfig.redisRateLimiter())
                                .setKeyResolver(rateLimiterConfig.ipKeyResolver())))
                        .uri(RouteConstants.HOSPITAL_SERVICE_URI))
                .route(RouteConstants.DOCTOR_SERVICE_ID, r -> r.path(RouteConstants.DOCTOR_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(rateLimiterConfig.redisRateLimiter())
                                .setKeyResolver(rateLimiterConfig.ipKeyResolver())))
                        .uri(RouteConstants.DOCTOR_SERVICE_URI))
                .route(RouteConstants.APPOINTMENT_SERVICE_ID, r -> r.path(RouteConstants.APPOINTMENT_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(rateLimiterConfig.redisRateLimiter())
                                .setKeyResolver(rateLimiterConfig.ipKeyResolver())))
                        .uri(RouteConstants.APPOINTMENT_SERVICE_URI))
                .build();
    }

}
