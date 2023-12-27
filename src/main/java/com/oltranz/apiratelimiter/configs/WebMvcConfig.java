package com.oltranz.apiratelimiter.configs;

import com.oltranz.apiratelimiter.interceptors.GlobalLimitInterceptor;
import com.oltranz.apiratelimiter.interceptors.RateLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final RateLimitInterceptor rateLimitInterceptor;
    private final GlobalLimitInterceptor systemLimitInterceptor;

    public WebMvcConfig(RateLimitInterceptor rateLimitInterceptor, GlobalLimitInterceptor systemLimitInterceptor) {
        this.systemLimitInterceptor = systemLimitInterceptor;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(systemLimitInterceptor).order(1); // intercepts all requests

        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/notifications/*").order(2); // only intercept notifications endpoints
    }
}
