package com.healthconnect.baseservice.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface RedisService {
    Mono<Void> storeTokenData(String token, Map<String, Object> data, long expirationTime);
    Mono<Map<Object, Object>> getTokenData(String token);
    Mono<Boolean> deleteTokenData(String token);
}
