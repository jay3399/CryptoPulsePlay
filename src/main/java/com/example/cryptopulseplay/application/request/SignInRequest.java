package com.example.cryptopulseplay.application.request;

import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.model.User.DeviceInfo;
import lombok.Getter;

@Getter
public class SignInRequest {

    private String email;
    private DeviceInfo deviceInfo;

    @Getter
    public static class DeviceInfo {

        private String browser;
        private String platform;

        public User.DeviceInfo toDomain() {
            return new User.DeviceInfo(browser, platform);
        }

    }
}
