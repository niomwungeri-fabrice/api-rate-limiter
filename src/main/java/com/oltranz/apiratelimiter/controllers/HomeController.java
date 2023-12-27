package com.oltranz.apiratelimiter.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
public class HomeController {
    @GetMapping("")
    public RedirectView home() {
        return new RedirectView("/swagger-ui/index.html");
    }
}
