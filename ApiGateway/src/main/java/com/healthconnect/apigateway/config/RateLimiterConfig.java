package com.healthconnect.apigateway.config;

import com.healthconnect.apigateway.constant.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimiterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterConfig.class);

    @Value("${rate-limiter.replenish-rate}")
    private int replenishRate;

    @Value("${rate-limiter.burst-capacity}")
    private int burstCapacity;

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
}
