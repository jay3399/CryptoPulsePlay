package com.example.cryptopulseplay.application.request;

import lombok.Getter;

@Getter
public class SignInRequest {

    private String email;
    private DeviceInfo deviceInfo;

    @Getter
    public static class DeviceInfo {

        private String browser;
        private String platform;

    }
}
