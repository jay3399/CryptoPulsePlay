package com.example.cryptopulseplay.application.exception.custom;

public class JwtValidationException extends RuntimeException{


    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
