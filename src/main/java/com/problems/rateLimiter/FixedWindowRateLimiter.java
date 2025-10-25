package com.problems.rateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FixedWindowRateLimiter implements RateLimiter {

    private static class Window {
        private final AtomicInteger count;
        private final AtomicLong windowStart;

        public Window() {
            this.count = new AtomicInteger(0);
            this.windowStart = new AtomicLong(System.currentTimeMillis());
        }
    }

    private final Map<String, Window> windows;
    private final int maxRequests;
    private final long windowSizeInMillis;

    public FixedWindowRateLimiter(int maxRequests, long timeWindowInSeconds) {
        this.windows = new ConcurrentHashMap<>();
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = timeWindowInSeconds*1000;
    }

    @Override
    public boolean allowRequest(String identifier) {
        Window window = windows.computeIfAbsent(identifier, k -> new Window());

        long currentTime = System.currentTimeMillis();
        long windowStart = window.windowStart.get();

        synchronized (window) {
            if (currentTime - window.windowStart.get() > windowSizeInMillis) {
                window.count.set(0);
                window.windowStart.set(currentTime);
                windowStart = currentTime;
            }
        }

        window.count.incrementAndGet();
        int currentCount = window.count.get();
        return currentCount <= maxRequests;
    }
}
