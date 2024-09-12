package com.healthconnect.cachingservice.service.impl;

import com.healthconnect.cachingservice.constants.LoggingConstants;
import com.healthconnect.cachingservice.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisCacheService implements CacheService {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);

    @Qualifier("redisCacheManager")
    private final RedisCacheManager redisCacheManager;

    public RedisCacheService(@Qualifier("redisCacheManager") RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }
    @Override
    public <T> T getFromCache(String cacheName, String key, Class<T> type) {
        log.debug(LoggingConstants.CACHE_PUT, cacheName, key);
        Cache cache = redisCacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                log.debug(LoggingConstants.CACHE_HIT, key, cacheName);
                return type.cast(valueWrapper.get());
            }
        }
        log.debug(LoggingConstants.CACHE_MISS, key, cacheName);
        return null;
    }

    @Override
    public void putInCache(String cacheName, String key, Object value) {
        log.debug(LoggingConstants.CACHE_PUT, cacheName, key);
        Cache cache = redisCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
            log.info(LoggingConstants.CACHE_PUT_SUCCESS, key, cacheName);
        }
    }

    @Override
    public void evictFromCache(String cacheName, String key) {
        log.debug(LoggingConstants.CACHE_EVICT, cacheName, key);
        Cache cache = redisCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            log.info(LoggingConstants.CACHE_EVICT_SUCCESS, key, cacheName);
        }
    }

    @Override
    public void clearCache(String cacheName) {
        log.info(LoggingConstants.CACHE_CLEAR, cacheName);
        Cache cache = redisCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.info(LoggingConstants.CACHE_CLEAR_SUCCESS, cacheName);
        }
    }

    @Override
    public <T> Mono<T> getFromCacheReactive(String cacheName, String key, Class<T> type) {
        return Mono.defer(() -> {
            log.debug(LoggingConstants.REACTIVE_CACHE_PUT, cacheName, key);
            T value = getFromCache(cacheName, key, type);
            if (value != null) {
                log.debug(LoggingConstants.REACTIVE_CACHE_HIT, key, cacheName);
            } else {
                log.debug(LoggingConstants.REACTIVE_CACHE_MISS, key, cacheName);
            }
            return value != null ? Mono.just(value) : Mono.empty();
        });
    }

    @Override
    public Mono<Void> putInCacheReactive(String cacheName, String key, Object value) {
        return Mono.fromRunnable(() -> {
            log.debug(LoggingConstants.REACTIVE_CACHE_PUT, cacheName, key);
            putInCache(cacheName, key, value);
        }).then();
    }

    @Override
    public Mono<Void> evictFromCacheReactive(String cacheName, String key) {
        return Mono.fromRunnable(() -> {
            log.debug(LoggingConstants.REACTIVE_CACHE_EVICT, cacheName, key);
            evictFromCache(cacheName, key);
        }).then();
    }

    @Override
    public Mono<Void> clearCacheReactive(String cacheName) {
        return Mono.fromRunnable(() -> {
            log.info(LoggingConstants.REACTIVE_CACHE_CLEAR, cacheName);
            clearCache(cacheName);
        }).then();
    }
}
