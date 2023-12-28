package com.oltranz.apiratelimiter.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BucketInfo {
    private long monthlyAvailable;
    private long timeWindowAvailable;
    private boolean monthlyAllowed;
    private boolean timeWindowBasedAllowed;
}
