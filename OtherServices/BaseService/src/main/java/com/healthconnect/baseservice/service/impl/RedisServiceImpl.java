package com.healthconnect.baseservice.service.impl;

import com.healthconnect.baseservice.service.RedisService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisServiceImpl(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> storeTokenData(String token, Map<String, Object> data, long expirationTime) {
        return redisTemplate.opsForHash().putAll(token, data)
                .then(redisTemplate.expire(token, Duration.ofSeconds(expirationTime - Instant.now().getEpochSecond())))
                .then();
    }

    @Override
    public Mono<Map<Object, Object>> getTokenData(String token) {
        return redisTemplate.opsForHash().entries(token).collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @Override
    public Mono<Boolean> deleteTokenData(String token) {
        return redisTemplate.delete(token).hasElement();
    }
}
