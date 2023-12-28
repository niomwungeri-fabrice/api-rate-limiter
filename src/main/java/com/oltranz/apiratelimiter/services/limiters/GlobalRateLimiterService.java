package com.oltranz.apiratelimiter.services.limiters;


import com.oltranz.apiratelimiter.configs.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class GlobalRateLimiterService {

    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final int MAX_REQUESTS_PER_MINUTE = Integer.parseInt(ConfigProperties.getMaxRequestPerMinute()); // maximum requests per 60 seconds

    @Scheduled(fixedRate = 60000) // Resets every minute
    public void resetRequestCount() {
        requestCount.set(0);
    }

    public boolean isLimitExceeded() {
        return requestCount.incrementAndGet() > MAX_REQUESTS_PER_MINUTE;
    }
}
