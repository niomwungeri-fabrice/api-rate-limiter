package com.oltranz.apiratelimiter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestLoggerDTO {
    private String title;
    private String body;

}
