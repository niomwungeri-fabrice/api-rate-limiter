package com.oltranz.apiratelimiter.enums;

import com.oltranz.apiratelimiter.configs.ConfigProperties;


public enum SubscriptionPlan {
    SUBSCRIPTION_FREE(Long.parseLong(ConfigProperties.getFreePlan())),
    SUBSCRIPTION_BASIC(Long.parseLong(ConfigProperties.getBasicPlan())),
    SUBSCRIPTION_PROFESSIONAL(Long.parseLong(ConfigProperties.getProPlan()));

    private final Long bucketLimit;

    SubscriptionPlan(Long bucketLimit) {
        this.bucketLimit = bucketLimit;
    }

    public Long getBucketLimitPerSeconds() {
        return this.bucketLimit;
    }

    public Long getBucketLimitPerMonth() {
        return this.bucketLimit * 5;
    }

}
