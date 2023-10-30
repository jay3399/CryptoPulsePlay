package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.application.ui.response.SignInResponse;
import com.example.cryptopulseplay.application.ui.response.TokenResponse;
import com.example.cryptopulseplay.application.ui.response.VerificationMessageResponse;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.shared.service.EmailService;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.model.User.DeviceInfo;
import com.example.cryptopulseplay.domian.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    private static final String LOGIN_CHECK = "loginCheck";


    public SignInResponse signInOrUp(String email, DeviceInfo deviceInfo) {

        User user = userService.findByEmail(email).orElse(User.create(email, deviceInfo));

        redisUtil.setUserByEmail(user);

        // 이메일 입력후 , 제출시 서버에서 판단후
        // 1.신규회원인지
        // 2.혹은 기존회원이지만 다른기기로이거나 , 이메일 인증기간이 특정시점을 넘어간 회원인지.
        // 둘을 구분해서 , 메세지로 출력.

        if (user.isReauthenticate(deviceInfo)) {

            emailService.sendVerificationEmail(user);

            return new VerificationMessageResponse(user);
        }

        // 인증자체가 필요없는경우 , 기기도 유효하고 신규회원도 아닌경우
        String loginToken = jwtUtil.generateToken(user, LOGIN_CHECK);

        generateRefreshToken(user);

        return new TokenResponse(loginToken);


    }

    private void generateRefreshToken(User user) {
        String refreshToken = jwtUtil.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userService.save(user);
    }


    public String verifyEmail(String token) {

        String accessToken = userService.verifyEmail(token);

        return getAlert(accessToken);
    }

    @Transactional
    public void finishGameOfUser(Long id) {
        User user = userService.findUser(id);
        user.finishGame();
    }


    private static String getAlert(String accessToken) {
        return "<html><script>"
                + "localStorage.setItem('loginToken', '" + accessToken + "');"
                + "alert('인증이 완료되었습니다.');"
                + "setTimeout(function() { window.location.href = '/mainPage'; }, 5000);"
                + "</script></html>";
    }

    // -> User 클래스로 , 객체 자신의 상태를 스스로 판단하고 관리.객체가 하나의 목적 역할에 집중, 응집도 ++

//    private boolean isReauthenticate(User user, String deviceInfo) {
//
//        if (!user.isEmailVerified()) {
//            return true;
//        }
//
//        if (isNewDevice(user, deviceInfo)) {
//            return true;
//        }
//
//        if (isLongtime(user)) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//    private static boolean isNewDevice(User user, String deviceInfo) {
//
//        if (deviceInfo != null && deviceInfo.equals(user.getDeviceInfo())) {
//            return false;
//        }
//
//        return true;
//    }
//
//    private static boolean isLongtime(User user) {
//
//        if (user.getEmailVerificationDate() != null) {
//
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime validationDate = user.getEmailVerificationDate();
//
//            Duration duration = Duration.between(validationDate, now);
//
//            long diffHours = duration.toHours();
//
//            return diffHours > 24;
//
//        }
//
//        return false;
//
//    }

    // 아래처럼 메서드로 쓰지말고 메세지 , 토큰생성 response를 만든뒤 , 그내 내부에서 해당로직실행.

//    private Map<String, String> generateAuthToken(User user) {
//
//        Map<String, String> authTokens = new HashMap<>();
//
//        user.setRefreshToken(refreshToken);
//
//        userService.save(user);
//
//        authTokens.put("loginToken", loginToken);
//
//        return authTokens;
//    }
//
//    private Map<String, String> sendEmailForVerification(User user) {
//
//        emailService.sendVerificationEmail(user);
//
//        Map<String, String> message = new HashMap<>();
//
//        if (!user.isEmailVerified()) {
//            message.put("message", "환영합니다 , 가입인증 메일을 전송하였습다 인증을 완료해주세요");
//        } else {
//            message.put("message", "로그인 인증 메일을 보냈습니다 확인해주세요");
//        }
//
//        return message;
//    }

//
//


}
