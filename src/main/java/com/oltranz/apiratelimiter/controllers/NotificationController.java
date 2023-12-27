package com.oltranz.apiratelimiter.controllers;

import com.oltranz.apiratelimiter.dto.ClientRequestLoggerDTO;
import com.oltranz.apiratelimiter.exceptions.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("notifications")
public class NotificationController {
    @PostMapping("notify")
    ResponseEntity<?> notify(@RequestBody ClientRequestLoggerDTO clientRequestLoggerDTO) {

        // TODO: actual call the notification service(sms|email|....)

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .data(clientRequestLoggerDTO)
                        .message("notification was sent successfully")
                        .build()
        );
    }
}
