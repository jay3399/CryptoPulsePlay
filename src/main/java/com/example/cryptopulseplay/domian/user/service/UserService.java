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


    @Transactional
    public String verifyEmail(String token) {

        String email = jwtUtil.getEmailFromToken(token);

        User user = redisUtil.getUser(email);

        String emailInRedis = redisUtil.getEmail(token);

        if (!email.equals(emailInRedis)) {
            throw new MailVerificationException();
        }

        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.markEmailAsVerified();

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
