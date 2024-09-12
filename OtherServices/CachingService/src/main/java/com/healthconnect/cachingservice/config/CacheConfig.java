package com.healthconnect.cachingservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.healthconnect.cachingservice.service.impl.HybridCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.caching.redis.ttl:600}") // Default TTL for Redis cache (10 mins)
    private long redisTtl;

    @Value("${app.caching.caffeine.ttl:600}") // Default TTL for Caffeine cache (10 mins)
    private long caffeineTtl;

    @Value("${app.caching.caffeine.max-size:500}") // Maximum size for Caffeine cache
    private int caffeineMaxSize;

    // Redis Configuration
    @Bean
    @Primary
    @Qualifier("redisCacheManager")
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(redisTtl))  // Set TTL
                .disableCachingNullValues()  // Do not cache null values
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    // RedisTemplate for manual operations
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer()); // Serialize keys as strings
        return template;
    }

    // Caffeine Cache Configuration
    @Bean
    @Qualifier("caffeineCacheManager")
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(caffeineTtl))  // Set TTL
                .maximumSize(caffeineMaxSize));  // Max size of cache
        return cacheManager;
    }
}
