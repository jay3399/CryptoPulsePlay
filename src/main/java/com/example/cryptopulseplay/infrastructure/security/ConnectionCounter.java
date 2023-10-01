package com.example.cryptopulseplay.infrastructure.security;


import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;


@Component
public class ConnectionCounter {


// WebFlux 는 비동기처리 . 여러 스레드에서 동시에 연결카운더 수정이가능

// AtomicInteger 를 이용 , 동시성문제 처리. -> 원자적 연산.

    private final AtomicInteger counter = new AtomicInteger();


    public int increment() {
        return counter.incrementAndGet();
    }

    public int decrement() {
        return counter.decrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }



}
