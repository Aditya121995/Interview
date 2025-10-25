package com.problems.rateLimiter;

public interface RateLimiter {
    boolean allowRequest(String identifier);
}
