package com.example.cryptopulseplay.domian.reword.model;


import lombok.Getter;

@Getter
public class Notification {

    private String message;

    private Long userId;

    public Notification(String message , Long userId) {
        this.message = message;
        this.userId = userId;

    }
}
