package com.problems.rateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterService {
    private final Map<String, RateLimiter> rateLimiterMap;
    private final RateLimitConfigManager rateLimitConfigManager;

    public RateLimiterService(RateLimitConfig defaultRateLimitConfig) {
        this.rateLimiterMap=new ConcurrentHashMap<>();
        this.rateLimitConfigManager = new RateLimitConfigManager(defaultRateLimitConfig);
    }

    public void configureRateLimiter(String rateLimiterName, RateLimitConfig rateLimitConfig) {
        rateLimitConfigManager.setConfig(rateLimiterName, rateLimitConfig);

        RateLimiter rateLimiter = RateLimitFactory.createRateLimiter(rateLimitConfig);
        rateLimiterMap.put(rateLimiterName, rateLimiter);
    }

    public void removeRateLimiter(String rateLimiterName) {
        rateLimiterMap.remove(rateLimiterName);
        rateLimitConfigManager.removeConfig(rateLimiterName);
    }

    public boolean allowRequest(String rateLimiterName) {
        if (!rateLimiterMap.containsKey(rateLimiterName)) {
            return true;
        }

        RateLimiter rateLimiter = rateLimiterMap.get(rateLimiterName);
        return rateLimiter.allowRequest(rateLimiterName);
    }
}
