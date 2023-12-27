package com.oltranz.apiratelimiter.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestLoggerDTO {
    private String title;
    private String body;

}
