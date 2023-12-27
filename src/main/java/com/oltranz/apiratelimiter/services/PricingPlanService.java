package com.oltranz.apiratelimiter.services;

import com.oltranz.apiratelimiter.dto.UpgradePlanDTO;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PricingPlanService {

    private final RedisService redisService;

    @Autowired
    public PricingPlanService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Client upgrade(UpgradePlanDTO upgradePlanDTO) throws IOException {
        String data = redisService.getValue(upgradePlanDTO.getClientId());
        Client client = Util.jsonToPojo(data, Client.class);
        // omit plan validation for brevity
        client.setPlan(upgradePlanDTO.getPlan());
        redisService.setValue(client.getClientId(), Util.pojoToJson(client));
        return client;
    }
}
