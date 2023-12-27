package com.oltranz.apiratelimiter.interceptors;

import com.oltranz.apiratelimiter.exceptions.RateLimitExceededException;
import com.oltranz.apiratelimiter.services.limiters.GlobalRateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class GlobalLimitInterceptor implements HandlerInterceptor {
    GlobalRateLimiterService globalRateLimiterService;

    @Autowired
    public GlobalLimitInterceptor(GlobalRateLimiterService globalRateLimiterService) {
        this.globalRateLimiterService = globalRateLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (globalRateLimiterService.isLimitExceeded()) {
            throw new RateLimitExceededException("maximum of 100 requests per minute is reached. please retry after 1 minute");
        }
        return true;
    }
}
