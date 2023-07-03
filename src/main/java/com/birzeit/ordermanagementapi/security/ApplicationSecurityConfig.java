package com.birzeit.ordermanagementapi.security;


import com.birzeit.ordermanagementapi.authentication.ApplicationUserService;
import com.birzeit.ordermanagementapi.exception.AuthEntryPoint;
import com.birzeit.ordermanagementapi.exception.ErrorDetails;
import com.birzeit.ordermanagementapi.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.time.LocalDateTime;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthEntryPoint authEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService,
                                     AuthenticationConfiguration authenticationConfiguration,
                                     AuthEntryPoint authEntryPoint,
                                     JwtAuthenticationFilter jwtAuthenticationFilter){
        super();
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/customers/signup").permitAll()
                .requestMatchers(HttpMethod.POST,"/login").permitAll()
//                .requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-resources/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    private ResponseEntity<ErrorDetails> handleExceptions() throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Unavailable",
                "Unavailable",
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.FORBIDDEN);
    }

}