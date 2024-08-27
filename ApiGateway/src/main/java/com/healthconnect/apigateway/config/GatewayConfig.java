package com.healthconnect.apigateway.config;

import com.healthconnect.apigateway.constant.MessageConstants;
import com.healthconnect.apigateway.constant.RouteConstants;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class GatewayConfig {

    @Value("${rate-limiter.replenish-rate}")
    private int replenishRate;

    @Value("${rate-limiter.burst-capacity}")
    private int burstCapacity;

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);

    private static final String REDIS_CONNECTION_TEST_KEY = "connection_test_key";
    private static final String REDIS_CONNECTION_TEST_VALUE = "connection_test_value";

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        LOGGER.info(MessageConstants.SETTING_UP_ROUTES_LOG);
        return builder.routes()
                .route(RouteConstants.USER_SERVICE_ID, r -> r.path(RouteConstants.USER_SERVICE_PATH)
                        .filters(f -> f.requestRateLimiter(config -> config
                                .setRateLimiter(redisRateLimiter())
                                .setKeyResolver(ipKeyResolver())))
                        .uri(RouteConstants.USER_SERVICE_URI))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        LOGGER.info(MessageConstants.REDIS_RATE_LIMITER_INITIALIZING);
        return new RedisRateLimiter(replenishRate, burstCapacity);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        LOGGER.info(MessageConstants.KEY_RESOLVER_INITIALIZING);
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

    @PostConstruct
    public void checkRedisConnection() {
        try {
            redisTemplate.opsForValue().set(REDIS_CONNECTION_TEST_KEY, REDIS_CONNECTION_TEST_VALUE).then().block();

            redisTemplate.hasKey(REDIS_CONNECTION_TEST_KEY).subscribe(hasKey -> {
                if (hasKey) {
                    LOGGER.info(MessageConstants.REDIS_CONNECTION_SUCCESS);
                } else {
                    LOGGER.warn(MessageConstants.REDIS_CONNECTION_FAILURE);
                }
            });

            redisTemplate.delete(REDIS_CONNECTION_TEST_KEY).subscribe();

        } catch (Exception e) {
            LOGGER.error(MessageConstants.REDIS_CONNECTION_ERROR, e);
        }
    }

}
