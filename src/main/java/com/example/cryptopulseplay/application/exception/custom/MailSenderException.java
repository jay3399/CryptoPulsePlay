package com.example.cryptopulseplay.application.exception.custom;

public class MailSenderException extends RuntimeException {

    public MailSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
