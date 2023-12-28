package com.oltranz.apiratelimiter.models;

import com.oltranz.apiratelimiter.enums.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private String clientId;
    private String name;
    private SubscriptionPlan plan;
    private long limitPerMinute;
    private long limitPerMonth;

}
