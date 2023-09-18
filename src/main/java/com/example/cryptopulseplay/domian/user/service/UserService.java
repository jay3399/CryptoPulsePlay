package com.example.cryptopulseplay.domian.user.service;

import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
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



    public String verifyEmail(String token) {

        String email = jwtUtil.getEmailFromToken(token).getSubject();

        User user = redisUtil.getUser(email);

        String emailInRedis = redisUtil.getEmail(token);

        if (emailInRedis == null || !email.equals(email)) {
            return null;
            //예외처리
        }

        if (!user.isEmailVerified()) {

            user.markEmailAsVerified(jwtUtil.generateRefreshToken(user));

            userRepository.save(user);
        }

        return jwtUtil.generateToken(user, "loginCheck");

    }





}
