package com.problems.rateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowRateLimiter implements RateLimiter {

    public static class RequestLog {
        private final ConcurrentLinkedDeque<Long> timestamps;

        public RequestLog() {
            this.timestamps = new ConcurrentLinkedDeque<>();
        }
    }

    private final Map<String, RequestLog> logs;
    private final int maxRequests;
    private final long windowSizeInMillis;

    public SlidingWindowRateLimiter(int maxRequests, long timeWindowInSeconds) {
        this.logs = new ConcurrentHashMap<>();
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = timeWindowInSeconds * 1000;
    }

    @Override
    public boolean allowRequest(String identifier) {
        RequestLog requestLog = logs.computeIfAbsent(identifier, k -> new RequestLog());

        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - windowSizeInMillis;

        synchronized (requestLog) {
            while (!requestLog.timestamps.isEmpty() &&
                    requestLog.timestamps.peekFirst() < startTime) {
                requestLog.timestamps.pollFirst();
            }

            requestLog.timestamps.addLast(currentTime);
        }

        int currentCount = requestLog.timestamps.size();
        return currentCount <= maxRequests;
    }
}
