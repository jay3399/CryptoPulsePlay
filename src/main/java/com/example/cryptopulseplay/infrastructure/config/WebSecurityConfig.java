package com.example.cryptopulseplay.infrastructure.config;

import com.example.cryptopulseplay.application.exception.custom.SecurityRedirectionException;
import com.example.cryptopulseplay.infrastructure.security.JwtAuthFilter;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final Set<String> permitAllEndpointSet = Set.of("/signIn", "/verifyEmail", "/", "/index.html", "/verifyLoginToken", "/btc-price", "/game", "/addPoint");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(
                        authz -> authz.requestMatchers((req) -> permitAllEndpointSet.contains(req.getRequestURI()) || req.getRequestURI().startsWith("/notifications/"))
                                .permitAll()
                                .requestMatchers((req) -> req.getRequestURI().startsWith("/admin")).hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, FilterSecurityInterceptor.class)
                .exceptionHandling(
                        e -> e.authenticationEntryPoint((request, response, authException)
                                        -> {
                                    try {
                                        response.sendRedirect("/");
                                    } catch (IOException ex) {
                                        throw new SecurityRedirectionException("failed to redirect to the URL on Security", ex);
                                    }

                                }
                        )
                );

        return httpSecurity.build();

    }


}
