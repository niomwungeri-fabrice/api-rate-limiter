package com.oltranz.apiratelimiter;


import com.oltranz.apiratelimiter.services.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ApiRateLimiterApplication {

    @Autowired
    RedisService redisService;

    public static void main(String[] args) {
        SpringApplication.run(ApiRateLimiterApplication.class, args);
    }


}
