package com.example.cryptopulseplay.domian.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String email;
    private String deviceInfo;
    private LocalDateTime emailVerificationDate;
    private int point = 0;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private boolean emailVerified = false;
    private String refreshToken;

    private static final long VERIFICATION_EXPIRATION_HOURS = 24;

    public boolean isReauthenticate(String deviceInfo) {

        if (!this.isEmailVerified()) {
            return true;
        }

        if (isNewDevice(deviceInfo)) {
            return true;
        }

        if (isLongtime()) {
            return true;
        }

        return false;

    }

    public boolean isNewDevice(String deviceInfo) {

        if (deviceInfo != null && deviceInfo.equals(this.deviceInfo)) {
            return false;
        }
        return true;
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


    public static User create(String email , String deviceInfo) {
        User user = new User();
        user.email = email;
        user.deviceInfo = deviceInfo;
        return user;
    }







}
