package com.oltranz.apiratelimiter.services.limiters;

import com.oltranz.apiratelimiter.dtos.responses.BucketInfo;
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
public class TokenBucketRateLimiterService {
    private final Map<String, Bucket> secondRateLimitBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> monthRateLimitBuckets = new ConcurrentHashMap<>();


    public BucketInfo allowRequest(Client client) {
        Bucket secondBucket = getBucketForSecondRateLimit(client);
        Bucket monthBucket = getBucketForMonthRateLimit(client);

        boolean consumeTimeWindowBucket = secondBucket.tryConsume(1);
        boolean consumeMonthlyBucket = monthBucket.tryConsume(1);

        return BucketInfo.builder()
                .timeWindowBasedAllowed(consumeTimeWindowBucket)
                .monthlyAllowed(consumeMonthlyBucket)
                .monthlyAvailable(monthBucket.getAvailableTokens())
                .timeWindowAvailable(secondBucket.getAvailableTokens())
                .build();
    }

    private Bucket getBucketForSecondRateLimit(Client client) {
        return secondRateLimitBuckets.computeIfAbsent(client.getClientId(), id -> {
            // Client-specific bucket initialization for per-second rate limiting
            return Bucket4j.builder()
                    .addLimit(Bandwidth.simple(client.getLimitPerMinute(), Duration.ofSeconds(60)))
                    .build();
        });
    }

    private Bucket getBucketForMonthRateLimit(Client client) {
        return monthRateLimitBuckets.computeIfAbsent(client.getClientId(), id -> {
            // Client-specific bucket initialization for per-month rate limiting
            return Bucket4j.builder()
                    .addLimit(Bandwidth.simple(client.getLimitPerMonth(), Duration.ofDays(30)))
                    .build();
        });
    }

    public void updateClientRateLimit(Client client) {
        // Remove old buckets
        secondRateLimitBuckets.remove(client.getClientId());
        monthRateLimitBuckets.remove(client.getClientId());

        // Initialize new buckets with updated limits
        getBucketForSecondRateLimit(client);
        getBucketForMonthRateLimit(client);
    }
}
