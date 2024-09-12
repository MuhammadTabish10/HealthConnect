package com.healthconnect.cachingservice.constants;

public class LoggingConstants {

    private LoggingConstants() {
    }

    // Cache-related logs
    public static final String CACHE_HIT = "Cache hit for key: {} in cache: {}";
    public static final String CACHE_MISS = "Cache miss for key: {} in cache: {}";
    public static final String CACHE_PUT = "Putting value in cache: {}, key: {}";
    public static final String CACHE_PUT_SUCCESS = "Value successfully cached for key: {} in cache: {}";
    public static final String CACHE_EVICT = "Evicting from cache: {}, key: {}";
    public static final String CACHE_EVICT_SUCCESS = "Cache eviction successful for key: {} in cache: {}";
    public static final String CACHE_CLEAR = "Clearing cache: {}";
    public static final String CACHE_CLEAR_SUCCESS = "Cache cleared successfully for cache: {}";

    // Reactive logs
    public static final String REACTIVE_CACHE_HIT = "Reactive cache hit for key: {} in cache: {}";
    public static final String REACTIVE_CACHE_MISS = "Reactive cache miss for key: {} in cache: {}";
    public static final String REACTIVE_CACHE_PUT = "Reactive put in cache: {}, key: {}";
    public static final String REACTIVE_CACHE_EVICT = "Reactive evict from cache: {}, key: {}";
    public static final String REACTIVE_CACHE_CLEAR = "Reactive clearing cache: {}";
}
