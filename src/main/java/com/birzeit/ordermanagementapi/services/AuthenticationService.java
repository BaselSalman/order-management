package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.dtos.JwtAuthResponseDTO;
import com.birzeit.ordermanagementapi.dtos.LoginDTO;
import com.birzeit.ordermanagementapi.jwt.JwtTokenProvider;
import com.birzeit.ordermanagementapi.security.PasswordConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final CustomerService customerService;

    private final AuthenticationManager authenticationManager;

    private final PasswordConfig passwordConfig;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(CustomerService customerService,
                                 AuthenticationManager authenticationManager,
                                 PasswordConfig passwordConfig,
                                 JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.passwordConfig = passwordConfig;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtAuthResponseDTO userLogin(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return new JwtAuthResponseDTO(token);
    }
}
