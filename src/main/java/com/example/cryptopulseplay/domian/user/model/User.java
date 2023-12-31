package com.example.cryptopulseplay.domian.user.model;

import com.example.cryptopulseplay.application.exception.custom.AlreadyParticipatingException;
import com.example.cryptopulseplay.application.exception.custom.InsufficientPointsException;
import com.example.cryptopulseplay.domian.game.model.Game;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded
    private DeviceInfo deviceInfo;
    private int point = 0;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private boolean emailVerified = false;

    private LocalDateTime emailVerificationDate;

    private LocalDateTime lastEmailVerification;

    private boolean isParticipatingInGame = false;


    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserGrade userGrade = UserGrade.SILVER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Game> games = new ArrayList<>();


    private User(String email, DeviceInfo deviceInfo) {
        this.email = email;
        this.deviceInfo = deviceInfo;
        this.role = Role.USER;
    }

    private static final long VERIFICATION_EXPIRATION_HOURS = 24;

    public boolean isReauthenticate(DeviceInfo deviceInfo) {

        return !this.isEmailVerified() || isNewDevice(deviceInfo) || isLongtime();

    }

    public boolean isNewDevice(DeviceInfo deviceInfo) {

        return !this.deviceInfo.equals(deviceInfo);

    }

    public boolean isLongtime() {

        if (this.getEmailVerificationDate() != null) {

            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(this.emailVerificationDate, now);

            long diffHours = duration.toHours();

            return diffHours > VERIFICATION_EXPIRATION_HOURS;

        }

        return false;

    }


    public void markEmailAsVerified() {
        this.emailVerified = true;
    }

    public void updateEmailVerifiedDateAndRefreshToken(String refreshToken) {
        this.emailVerificationDate = LocalDateTime.now();
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public void updateLastEmailVerificationRequestTime() {
        this.lastEmailVerification = LocalDateTime.now();
    }
    public boolean isEmailVerificationLimited() {

        if (lastEmailVerification == null) {
            return false;
        }

        return Duration.between(lastEmailVerification, LocalDateTime.now()).toMinutes() < 15;
    }

    public void resetEmailVerificationLimit() {
        lastEmailVerification = null;
    }




    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class DeviceInfo implements Serializable {

        private String browser;
        private String platform;

    }

    // Dirty Checking ; 변경감지.
    public void playGame(int amount) {

        checkParticipationStatus();
        validateSufficientPoints(amount);
        decreasePoints(amount);

    }

    private void decreasePoints(int amount) {
        this.point -= amount;
    }


    //Reword 지급
    public void updatePoints(int point) {
        this.point += point;
        updateGrade();
    }

    private void updateGrade() {
        if (this.point >= 1000) {
            this.userGrade = UserGrade.GOLD;
        } else if (this.point >= 500) {
            this.userGrade = UserGrade.SILVER;
        } else {
            this.userGrade = UserGrade.BRONZE;
        }
    }

    private void checkParticipationStatus() {
        if (this.isParticipatingInGame) {
            System.out.println("이미참여");
            throw new AlreadyParticipatingException("you are already participation in this game");
        }
        this.isParticipatingInGame = true;
    }


    private void validateSufficientPoints(int amount) {
        if (this.point < amount) {
            throw new InsufficientPointsException("Not enough point to play");
        }
    }

    public void finishGame() {
        this.isParticipatingInGame = false;

    }

    public static User create(String email, DeviceInfo deviceInfo) {
        return new User(email, deviceInfo);
    }


}
