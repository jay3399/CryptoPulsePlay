package com.example.cryptopulseplay.infrastructure.config;

import com.example.cryptopulseplay.infrastructure.security.JwtAuthFilter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final Set<String> permitAllEndpointSet = Set.of("/signIn", "/verifyEmail", "/");


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(
                authz -> authz.requestMatchers(
                        req -> permitAllEndpointSet.contains(req.getRequestURI()))
                        .permitAll()
                        .anyRequest().authenticated()

        ).addFilterBefore(jwtAuthFilter, null);

        return httpSecurity.build();

    }






}
