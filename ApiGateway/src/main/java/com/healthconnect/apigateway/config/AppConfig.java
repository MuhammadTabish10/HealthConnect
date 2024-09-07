package com.healthconnect.apigateway.config;

import com.healthconnect.baseservice.service.RedisService;
import com.healthconnect.baseservice.service.impl.RedisServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

@Configuration
public class AppConfig {

    private final ReactiveStringRedisTemplate redisTemplate;

    public AppConfig(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public RedisService redisService() {
        return new RedisServiceImpl(redisTemplate);
    }
}
