package com.example.cryptopulseplay.application.ui.response;



public class ErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    private ErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public static ErrorResponse create(int status, String message) {

        return new ErrorResponse(status, message, System.currentTimeMillis());

    }




}
