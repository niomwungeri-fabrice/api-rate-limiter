package com.oltranz.apiratelimiter.dtos.requests;

import com.oltranz.apiratelimiter.enums.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradePlanDTO {
    private String clientId;
    private SubscriptionPlan plan;
}
