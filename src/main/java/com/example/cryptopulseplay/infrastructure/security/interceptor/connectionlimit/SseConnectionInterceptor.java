package com.example.cryptopulseplay.infrastructure.security.interceptor.connectionlimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SseConnectionInterceptor implements HandlerInterceptor {

    private static final int MEX_CON = 2;

    private final ConnectionCounter connectionCounter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        if ("/btc-price".equals(request.getRequestURI()) && connectionCounter.getCount() >= MEX_CON) {

            System.out.println("Con MAX");
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            return false;
        }

        if ("/btc-price".equals(request.getRequestURI())) {
            System.out.println("CON");
            connectionCounter.increment();
            System.out.println("connectionCounter = " + connectionCounter.getCount());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        if ("/btc-price".equals(request.getRequestURI())) {
            connectionCounter.decrement();
        }
    }
}
