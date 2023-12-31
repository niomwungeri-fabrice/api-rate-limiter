package com.oltranz.apiratelimiter.services;

import com.oltranz.apiratelimiter.dtos.requests.ClientRequestDTO;
import com.oltranz.apiratelimiter.enums.SubscriptionPlan;
import com.oltranz.apiratelimiter.exceptions.ConflictException;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class ClientService {

    private final RedisService redisService;

    @Autowired
    public ClientService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Client handleClientRegistration(ClientRequestDTO clientDTO) throws NoSuchAlgorithmException {
        Client client = Client.builder()
                .clientId(Util.generateUsername(clientDTO.getName()))
                .name(clientDTO.getName())
                .plan(SubscriptionPlan.SUBSCRIPTION_BASIC) // initial state
                .limitPerMinute(SubscriptionPlan.SUBSCRIPTION_BASIC.getBucketLimitPerMinute())
                .limitPerMonth(SubscriptionPlan.SUBSCRIPTION_BASIC.getBucketLimitPerMonth())
                .build();
        // save to redis
        String data = redisService.getValue(Util.generateUsername(client.getName()));
        if (data == null) {
            redisService.setValue(Util.generateUsername(client.getName()), Util.pojoToJson(client));
            return client;
        } else {
            throw new ConflictException(String.format("client already exist %s", Util.generateUsername(client.getName())));
        }
    }
}
