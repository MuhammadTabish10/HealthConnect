package com.healthconnect.cachingservice.service;

import reactor.core.publisher.Mono;

public interface CacheService {
    <T> T getFromCache(String cacheName, String key, Class<T> type);
    void putInCache(String cacheName, String key, Object value);
    void evictFromCache(String cacheName, String key);
    void clearCache(String cacheName);

    <T> Mono<T> getFromCacheReactive(String cacheName, String key, Class<T> type);
    Mono<Void> putInCacheReactive(String cacheName, String key, Object value);
    Mono<Void> evictFromCacheReactive(String cacheName, String key);
    Mono<Void> clearCacheReactive(String cacheName);
}
