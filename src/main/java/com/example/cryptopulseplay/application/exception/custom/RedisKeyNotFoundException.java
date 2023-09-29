package com.example.cryptopulseplay.application.exception.custom;

public class RedisKeyNotFoundException extends RuntimeException {

    public RedisKeyNotFoundException(String key) {
        super("Redis Key not found:" + key);
    }

}
