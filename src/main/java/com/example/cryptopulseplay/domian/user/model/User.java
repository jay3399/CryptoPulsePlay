package com.example.cryptopulseplay.domian.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder.Default;
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

    public void markEmailAsVerified(User user) {
        user.emailVerified = true;
        user.emailVerificationDate = LocalDateTime.now();
    }

    public void setRefreshToken(User user, String refreshToken) {

        user.refreshToken = refreshToken;
    }




    public static User create(String email , String deviceInfo) {
        User user = new User();
        user.email = email;
        user.deviceInfo = deviceInfo;
        return user;
    }







}
