package com.oltranz.apiratelimiter.enums;

import com.oltranz.apiratelimiter.configs.ConfigProperties;


public enum SubscriptionPlan {
    SUBSCRIPTION_BASIC(Long.parseLong(ConfigProperties.getBasicPlan())),
    SUBSCRIPTION_PRO(Long.parseLong(ConfigProperties.getProPlan()));

    private final Long bucketLimit;

    SubscriptionPlan(Long bucketLimit) {
        this.bucketLimit = bucketLimit;
    }

    public Long getBucketLimitPerMinute() {
        return this.bucketLimit;
    }

    public Long getBucketLimitPerMonth() {
        return this.bucketLimit * Long.parseLong(ConfigProperties.getPlanMultiplier());
    }

}
