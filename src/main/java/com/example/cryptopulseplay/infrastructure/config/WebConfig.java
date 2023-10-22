package com.example.cryptopulseplay.infrastructure.config;

import com.example.cryptopulseplay.infrastructure.security.interceptor.connectionlimit.SseConnectionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SseConnectionInterceptor sseConnectionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(sseConnectionInterceptor).addPathPatterns("/btc-price");
    }
}
