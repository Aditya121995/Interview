package com.problems.rateLimiter;

public class RateLimitFactory {
    public static RateLimiter createRateLimiter(RateLimitConfig config) {
        switch (config.getRateLimitStrategy()) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(config.getMaxRequest(), config.getTimeWindowInSeconds());
            case SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(config.getMaxRequest(), config.getTimeWindowInSeconds());
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config.getMaxRequest(), config.getTimeWindowInSeconds());
            default:
                throw new IllegalArgumentException("Invalid rateLimitStrategy");
        }
    }
}
