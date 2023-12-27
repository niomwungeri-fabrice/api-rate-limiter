package com.oltranz.apiratelimiter.controllers;

import com.oltranz.apiratelimiter.dto.UpgradePlanDTO;
import com.oltranz.apiratelimiter.exceptions.ApiResponse;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.services.PricingPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("rate")
public class RateLimitManagerController {

    PricingPlanService rateLimitManagerService;

    @Autowired
    public RateLimitManagerController(PricingPlanService rateLimitManagerService) {
        this.rateLimitManagerService = rateLimitManagerService;
    }

    @PostMapping("upgrade")
    ResponseEntity<?> increaseLimit(@RequestBody UpgradePlanDTO upgradePlanDTO) throws IOException {
        Client client = rateLimitManagerService.upgrade(upgradePlanDTO);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .data(client)
                        .message(String.format("client %s has been upgraded to %s", client.getClientId(), client.getPlan().toString()))
                        .build()
        );
    }


}
