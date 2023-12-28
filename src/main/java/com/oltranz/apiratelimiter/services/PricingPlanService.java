package com.oltranz.apiratelimiter.services;

import com.oltranz.apiratelimiter.dtos.requests.UpgradePlanDTO;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.services.limiters.TokenBucketRateLimiterService;
import com.oltranz.apiratelimiter.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PricingPlanService {

    private final RedisService redisService;
    private final TokenBucketRateLimiterService rateLimiterService;

    @Autowired
    public PricingPlanService(RedisService redisService, TokenBucketRateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
        this.redisService = redisService;
    }

    public Client upgrade(UpgradePlanDTO upgradePlanDTO) throws IOException {
        String data = redisService.getValue(upgradePlanDTO.getClientId());
        Client client = Util.jsonToPojo(data, Client.class);
        // omit plan validation for brevity
        client.setPlan(upgradePlanDTO.getPlan());
        client.setLimitPerMinute(upgradePlanDTO.getPlan().getBucketLimitPerMinute());
        client.setLimitPerMonth(upgradePlanDTO.getPlan().getBucketLimitPerMonth());
        redisService.setValue(client.getClientId(), Util.pojoToJson(client));
        rateLimiterService.updateClientRateLimit(client);
        return client;
    }
}
