package com.birzeit.ordermanagementapi.exception;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
                         jakarta.servlet.http.HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException
    ) throws IOException, ServletException {
        System.out.println(authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                authException.getMessage(),
                authException.getLocalizedMessage(),
                HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("{\n" +
                "\"timeStamp\": " + "\"" + errorDetails.getTimeStamp() + "\"" + ",\n" +
                "\"message\": " + "\"" + "Access denied. Please log in." + "\"" + ",\n" +
                "\"details\": " + "\"" + errorDetails.getDetails() + "\"" + ",\n" +
                "\"status\": " +  errorDetails.getStatus() +
                "\n}");
    }
}