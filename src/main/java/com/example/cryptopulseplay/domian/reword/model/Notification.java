package com.example.cryptopulseplay.domian.reword.model;


import lombok.Getter;

@Getter
public class Notification {

    private String message;

    public Notification(String message) {
        this.message = message;
    }
}
