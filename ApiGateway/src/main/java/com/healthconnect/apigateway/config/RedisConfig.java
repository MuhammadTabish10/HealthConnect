package com.healthconnect.apigateway.config;

import com.healthconnect.apigateway.constant.MessageConstants;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

@Configuration
public class RedisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    private static final String REDIS_CONNECTION_TEST_KEY = "connection_test_key";
    private static final String REDIS_CONNECTION_TEST_VALUE = "connection_test_value";

    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisConfig(ReactiveRedisConnectionFactory factory) {
        this.redisTemplate = new ReactiveStringRedisTemplate(factory);
    }

    @Bean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @PostConstruct
    public void checkRedisConnection() {
        try {
            redisTemplate.opsForValue().set(REDIS_CONNECTION_TEST_KEY, REDIS_CONNECTION_TEST_VALUE).then().block();

            redisTemplate.hasKey(REDIS_CONNECTION_TEST_KEY).subscribe(hasKey -> {
                if (Boolean.TRUE.equals(hasKey)) {
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
