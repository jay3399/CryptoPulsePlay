package com.example.cryptopulseplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CryptoPulsePlayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPulsePlayApplication.class, args);
    }

}
