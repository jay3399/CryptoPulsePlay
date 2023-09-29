package com.example.cryptopulseplay.application.exception.custom;

public class MailVerificationException extends RuntimeException {

    public MailVerificationException() {
        super("email verification error");
    }
}
