package com.oltranz.apiratelimiter.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {
    private static ConfigProperties configProperties;

    @Value("${subscription.free.plan}")
    private String freePlan;
    @Value("${subscription.basic.plan}")
    private String basicPlan;
    @Value("${subscription.pro.plan}")
    private String proPlan;


    public ConfigProperties() {
        configProperties = this;
    }

    public static String getBasicPlan() {
        return configProperties.basicPlan;
    }

    public static String getProPlan() {
        return configProperties.proPlan;
    }

    public static String getFreePlan() {
        return configProperties.freePlan;
    }
}