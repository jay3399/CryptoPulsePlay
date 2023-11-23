package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.application.ui.response.DuplicateMessageResponse;
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


    /**
     * 이메일과 , 디바이스 정보를 받은후
     * 유저가 디비에 있을시에는 그대로 가져오고 ,없을시 새로 생성합니다 .
     * 디바이스 정보 , 이메일 인증시간 등을 비교해서 인증이 필요할시 이메일 인증메일을 보낸다.
     * 이메일 인증이 필요없을시 , 로그인토큰을 만들고 반환.
     * @param email 유저이메일
     * @param deviceInfo 이메일 인증시 필요한 디바이스정보.
     * @return
     */
    public SignInResponse signIn(String email, DeviceInfo deviceInfo) {

        User user = userService.findByEmail(email).orElse(User.create(email, deviceInfo));

        if (user.isEmailVerificationLimited()) {
            return new DuplicateMessageResponse();
        }


        if (user.isReauthenticate(deviceInfo)) {

            redisUtil.setUserByEmail(user);

            emailService.sendVerificationEmail(user);

            user.updateLastEmailVerificationRequestTime();

            return new VerificationMessageResponse(user);
        }


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
}
