package com.oltranz.apiratelimiter.interceptors;

import com.oltranz.apiratelimiter.exceptions.BadRequestException;
import com.oltranz.apiratelimiter.exceptions.RateLimitExceededException;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.services.RedisService;
import com.oltranz.apiratelimiter.services.limiters.TokenBucketRateLimiterService;
import com.oltranz.apiratelimiter.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final TokenBucketRateLimiterService rateLimiterService;
    private final RedisService redisService;

    @Autowired
    public RateLimitInterceptor(TokenBucketRateLimiterService rateLimiterService, RedisService redisService) {
        this.rateLimiterService = rateLimiterService;
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String clientId = request.getHeader("client-id");
        if (clientId == null) {
            throw new BadRequestException("client id is required");
        }
        String data = redisService.getValue(clientId);
        Client client = Util.jsonToPojo(data, Client.class);

        if (!rateLimiterService.allowRequestSeconds(client)) {
            throw new RateLimitExceededException("too many requests per minute please wait 1 minute to try again or upgrade your limit");
        }
        if (!rateLimiterService.allowRequestMonth(client)) {
            throw new RateLimitExceededException("too many requests per month please wait the 30 days try again or upgrade your limit");
        }
        return true;
    }
}
