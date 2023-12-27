package com.oltranz.apiratelimiter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ApiRateLimiterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiRateLimiterApplication.class, args);
    }


}
