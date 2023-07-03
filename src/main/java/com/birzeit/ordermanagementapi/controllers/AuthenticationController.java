package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.CustomerResponseDTO;
import com.birzeit.ordermanagementapi.dtos.JwtAuthResponseDTO;
import com.birzeit.ordermanagementapi.dtos.LoginDTO;
import com.birzeit.ordermanagementapi.entities.User;
import com.birzeit.ordermanagementapi.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public JwtAuthResponseDTO login(@Valid @RequestBody LoginDTO loginDTO) throws URISyntaxException {
        return authenticationService.userLogin(loginDTO);
    }
}
