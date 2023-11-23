package com.example.cryptopulseplay.domian.user.service;

import com.example.cryptopulseplay.application.exception.custom.MailVerificationException;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;


    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }


    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }


    /**
     * 로그인 / 가입 이메일인증
     * 요청 토큰에서 꺼낸 이메일값과 , 레디스에 저장된 해당 토큰의 이메일을 비교후
     * 성공시 ,Redis 에서 유저를 가져와서 이메일인증 상태 업데이트후, 유저를 디비에 영속화.
     * @param token
     * @return
     */
    @Transactional
    public String verifyEmail(String token) {

        String email = jwtUtil.getEmailFromToken(token);

        String emailInRedis = redisUtil.getEmail(token);

        if (!email.equals(emailInRedis)) {
            throw new MailVerificationException();
        }

        User user = redisUtil.getUser(email);

        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.markEmailAsVerified();

        user.resetEmailVerificationLimit();

        user.updateEmailVerifiedDateAndRefreshToken(refreshToken);

        userRepository.save(user);

        return jwtUtil.generateToken(user, "loginCheck");

    }

    public User findUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Id" + userId + "not found"));

    }

    @Transactional
    public void addPoint(Long userId, int point) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Id" + userId + "not found"));

        user.updatePoints(point);


    }







}
