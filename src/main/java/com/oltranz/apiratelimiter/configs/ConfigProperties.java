package com.oltranz.apiratelimiter.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {
    private static ConfigProperties configProperties;

    @Value("${subscription.basic.plan}")
    private String basicPlan;
    @Value("${subscription.pro.plan}")
    private String proPlan;

    @Value("${subscription.monthly.multiplier}")
    private String planMultiplier;
    @Value("${max.request.per.minute}")
    private String maxRequestPerMinute;

    public ConfigProperties() {
        configProperties = this;
    }

    public static String getBasicPlan() {
        return configProperties.basicPlan;
    }

    public static String getProPlan() {
        return configProperties.proPlan;
    }

    public static String getPlanMultiplier() {
        return configProperties.planMultiplier;
    }

    public static String getMaxRequestPerMinute() {
        return configProperties.maxRequestPerMinute;
    }
}