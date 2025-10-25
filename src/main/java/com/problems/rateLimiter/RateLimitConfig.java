package com.problems.rateLimiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RateLimitConfig {
    private final int maxRequest;
    private final int timeWindowInSeconds;
    private final RateLimitStrategy rateLimitStrategy;


    public RateLimitConfig(int maxRequest, int timeWindowInSeconds, RateLimitStrategy rateLimitStrategy) {
        this.maxRequest = maxRequest;
        this.timeWindowInSeconds = timeWindowInSeconds;
        this.rateLimitStrategy = rateLimitStrategy;
    }
}
