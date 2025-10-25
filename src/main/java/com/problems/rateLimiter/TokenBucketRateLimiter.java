package com.problems.rateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketRateLimiter implements RateLimiter {

    public static class Bucket {
        private double tokens;
        private final int capacity;
        private long lastRefilledTimestampInMillis;

        public Bucket(int capacity) {
            this.tokens = capacity;
            this.capacity = capacity;
            this.lastRefilledTimestampInMillis = System.currentTimeMillis();
        }
    }

    private final Map<String, Bucket> buckets;
    private final int bucketCapacity;
    private final long windowSizeInMillis;
    private final double refillRate;

    public TokenBucketRateLimiter(int bucketCapacity, long timeWindowInSeconds) {
        this.bucketCapacity = bucketCapacity;
        this.windowSizeInMillis = timeWindowInSeconds*1000;
        this.buckets = new ConcurrentHashMap<>();
        this.refillRate = (double) bucketCapacity/windowSizeInMillis ;
    }

    @Override
    public boolean allowRequest(String identifier) {
        Bucket bucket = buckets.computeIfAbsent(identifier, k -> new Bucket(bucketCapacity));

        synchronized (bucket) {
            refillBucket(bucket);
        }

        if (bucket.tokens >= 1.0) {
            bucket.tokens -=1;
            return true;
        }
        return false;
    }

    private void refillBucket(Bucket bucket) {
        long currentTime = System.currentTimeMillis();
        long lastRefilledTimestampInMillis = bucket.lastRefilledTimestampInMillis;
        long elapsedTime = currentTime - lastRefilledTimestampInMillis;
        double tokenToAdd = (elapsedTime/1000.0) * refillRate;

        bucket.tokens = Math.min(bucketCapacity, bucket.tokens + tokenToAdd);
        bucket.lastRefilledTimestampInMillis = currentTime;
    }
}
