package com.oltranz.apiratelimiter.controllers;

import com.oltranz.apiratelimiter.dtos.requests.ClientRequestDTO;
import com.oltranz.apiratelimiter.dtos.responses.ApiResponse;
import com.oltranz.apiratelimiter.models.Client;
import com.oltranz.apiratelimiter.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;


    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping("register")
    ResponseEntity<?> notify(@RequestBody ClientRequestDTO clientDTO) throws NoSuchAlgorithmException {
        Client client = clientService.handleClientRegistration(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .data(client)
                        .message("Registration was done successfully")
                        .status(HttpStatus.CREATED.value())
                        .build()
        );
    }
}
