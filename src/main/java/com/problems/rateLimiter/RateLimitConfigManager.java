package com.problems.rateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitConfigManager {
    private final Map<String, RateLimitConfig> rateLimitConfigMap;
    private final RateLimitConfig defaultConfig;

    public RateLimitConfigManager(RateLimitConfig defaultConfig) {
        this.rateLimitConfigMap = new ConcurrentHashMap<>();
        this.defaultConfig = defaultConfig;
    }

    public void setConfig(String rateLimiterName, RateLimitConfig config) {
        rateLimitConfigMap.put(rateLimiterName, config);
    }

    public RateLimitConfig getConfig(String rateLimiterName) {
        return rateLimitConfigMap.getOrDefault(rateLimiterName, null);
    }

    public void removeConfig(String rateLimiterName) {
        rateLimitConfigMap.remove(rateLimiterName);
    }
}
