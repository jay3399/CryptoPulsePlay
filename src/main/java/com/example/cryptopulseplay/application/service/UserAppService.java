package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.shared.JwtUtil;
import com.example.cryptopulseplay.domian.shared.service.EmailService;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.service.UserService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;


    public Map<String, String> signInOrUp(String email, String deviceInfo) {

        User user = userService.findByEmail(email).orElse(User.create(email, deviceInfo));

        redisTemplate.opsForValue().set("user" + email, user);

        Map<String, String> tokens = new HashMap<>();

        if (isReauthenticate(user, deviceInfo)) {

            String token = jwtUtil.generateToken(email, "emailCheck");

            redisTemplate.opsForValue().set(token, email);

            emailService.sendVerificationEmail(email, token);

            if (!user.isEmailVerified()) {
                tokens.put("message", "환영합니다 , 가입인증 메일을 전송하였습다 인증을 완료해주세요");
            } else {
                tokens.put("message", "로그인 인증 메일을 보냈습니다 확인해주세요");
            }
        } else {

            String loginToken = jwtUtil.generateToken(user, "loginCheck");
            String refreshToken = jwtUtil.generateRefreshToken(user);

            user.setRefreshToken(user, refreshToken);

            userService.save(user);

            tokens.put("loginToken", loginToken);
            tokens.put("refreshToken", refreshToken);
        }

        return tokens;

    }


    public String verifyEmail(String token) {


        String email = jwtUtil.validateToken(token).getSubject();

        User user = (User) redisTemplate.opsForValue().get("user:" + email);

        String storedEmail = (String) redisTemplate.opsForValue().get(token);

        if (storedEmail == null || !email.equals(storedEmail)) {
            return null;
            //예외 반환
        }

        if (!user.isEmailVerified()) {
            user.markEmailAsVerified(user);
            userService.save(user);
        }

        return jwtUtil.generateToken(user, "loginCheck");



    }

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
