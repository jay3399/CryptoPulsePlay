package com.example.cryptopulseplay.domian.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String email;

    @Embedded
    private DeviceInfo deviceInfo;
    private LocalDateTime emailVerificationDate;
    private int point = 0;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private boolean emailVerified = false;
    private String refreshToken;

    private static final long VERIFICATION_EXPIRATION_HOURS = 24;

    public boolean isReauthenticate(DeviceInfo deviceInfo) {

        return !this.isEmailVerified() || isNewDevice(deviceInfo) || isLongtime();

    }

    public boolean isNewDevice(DeviceInfo deviceInfo) {


        return !this.deviceInfo.equals(deviceInfo);

    }

    public  boolean isLongtime() {

        if (this.getEmailVerificationDate() != null) {

            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(this.emailVerificationDate, now);

            long diffHours = duration.toHours();

            return diffHours > VERIFICATION_EXPIRATION_HOURS;

        }

        return false;

    }


    public void markEmailAsVerified(String refreshToken) {
        this.emailVerified = true;
        this.emailVerificationDate = LocalDateTime.now();
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class DeviceInfo implements Serializable{
        private String browser;
        private String platform;

    }

    public static User create(String email , DeviceInfo deviceInfo) {
        User user = new User();
        user.email = email;
        user.deviceInfo = deviceInfo;
        return user;
    }







}
