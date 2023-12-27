package com.oltranz.apiratelimiter.services.limiters;

import com.oltranz.apiratelimiter.models.Client;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class TokenBucketRateLimiterService  {
    private final Map<String, Bucket> secondRateLimitBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> monthRateLimitBuckets = new ConcurrentHashMap<>();

    public boolean allowRequestSeconds(Client client) {
        Bucket bucket = getBucketForSecondRateLimit(client.getClientId(), client.getPlan().getBucketLimitPerSeconds());
        return bucket.tryConsume(1);
    }


    public boolean allowRequestMonth(Client client) {
        Bucket bucket = getBucketForMonthRateLimit(client.getClientId(), client.getPlan().getBucketLimitPerMonth());
        return bucket.tryConsume(1);
    }

    private Bucket getBucketForSecondRateLimit(String clientId, long limit) {
        return secondRateLimitBuckets.computeIfAbsent(clientId, id -> {
            // Client-specific bucket initialization for per-second rate limiting
            return Bucket4j.builder()
                    .addLimit(Bandwidth.simple(limit, Duration.ofMinutes(60)))
                    .build();
        });
    }

    private Bucket getBucketForMonthRateLimit(String clientId, long limit) {
        return monthRateLimitBuckets.computeIfAbsent(clientId, id -> {
            // Client-specific bucket initialization for per-month rate limiting
            return Bucket4j.builder()
                    .addLimit(Bandwidth.simple(limit, Duration.ofDays(30)))
                    .build();
        });
    }
}
