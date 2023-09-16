package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.shared.service.EmailService;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.service.UserService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    public Map<String, String> signInOrUp(String email, String deviceInfo) {

        User user = userService.findByEmail(email).orElse(User.create(email, deviceInfo));

        redisUtil.setUserByEmail(email, user);

        Map<String, String> tokens = new HashMap<>();


        if (user.isReauthenticate(deviceInfo)) {

            String token = jwtUtil.generateToken(email, "emailCheck");

            redisUtil.setTokenByEmail(token, email);

            emailService.sendVerificationEmail(email, token);

            if (!user.isEmailVerified()) {
                tokens.put("message", "환영합니다 , 가입인증 메일을 전송하였습다 인증을 완료해주세요");
            } else {
                tokens.put("message", "로그인 인증 메일을 보냈습니다 확인해주세요");
            }
        } else {

            String loginToken = jwtUtil.generateToken(user, "loginCheck");
            String refreshToken = jwtUtil.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);

            userService.save(user);

            tokens.put("loginToken", loginToken);
            tokens.put("refreshToken", refreshToken);
        }

        return tokens;

    }


    public String verifyEmail(String token) {

        String accessToken = userService.verifyEmail(token);

        if (accessToken != null) {

            return "<html><script>"
                    + "localStorage.setItem('loginToken', '" + accessToken + "');"
                    + "alert('인증이 완료되었습니다.');"
                    + "setTimeout(function() { window.location.href = '/mainPage'; }, 5000);"
                    + "</script></html>";
        } else {
            return null;
            //예외
        }


    }

    // -> User 클래스로 , 객체 자신의 상태를 스스로 판단하고 관리.객체가 하나의 목적 역할에 집중, 응집도 ++
    private boolean isReauthenticate(User user, String deviceInfo) {

        if (!user.isEmailVerified()) {
            return true;
        }

        if (isNewDevice(user, deviceInfo)) {
            return true;
        }

        if (isLongtime(user)) {
            return true;
        }

        return false;

    }

    private static boolean isNewDevice(User user, String deviceInfo) {

        if (deviceInfo != null && deviceInfo.equals(user.getDeviceInfo())) {
            return false;
        }

        return true;
    }

    private static boolean isLongtime(User user) {

        if (user.getEmailVerificationDate() != null) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime validationDate = user.getEmailVerificationDate();

            Duration duration = Duration.between(validationDate, now);

            long diffHours = duration.toHours();

            return diffHours > 24;

        }

        return false;

    }




}
