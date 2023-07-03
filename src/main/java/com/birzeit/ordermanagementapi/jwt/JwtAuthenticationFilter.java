package com.birzeit.ordermanagementapi.jwt;

import com.birzeit.ordermanagementapi.LocalDateTimeFormatter;
import com.birzeit.ordermanagementapi.authentication.ApplicationUserService;
import com.birzeit.ordermanagementapi.exception.BadRequestException;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//Extend OncePerRequestFilter to ensure process each dispatched
// request only once
//JwtAuthenticationFilter validates the Token using
// JwtTokenProvider.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // inject dependencies
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, BadRequestException {

        try {
            // get JWT (token) from http request
            String token = getJwtFromRequest(request);
            // validate token
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                // get username from token
                String username = tokenProvider.getUsernameFromJWT(token);
                // load user associated with token
                UserDetails userDetails = applicationUserService.loadUserByUsername(username);

                // create AuthenticationToken
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // set spring security, Store Authentication object in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (BadRequestException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("timeStamp", LocalDateTimeFormatter.formatDate(LocalDate.now()));
            errorMap.put("message", ex.getMessage());
            errorMap.put("status", HttpStatus.BAD_REQUEST.value());
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(errorMap));
            return;
        }
        filterChain.doFilter(request, response);
    }

    // Bearer <accessToken>
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}