package com.example.cryptopulseplay.domian.shared.util;

import com.example.cryptopulseplay.application.exception.custom.RedisKeyNotFoundException;
import com.example.cryptopulseplay.domian.user.model.User;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public final RedisTemplate redisTemplate;

    private static final String USER_KEY = "user:";

    public void setUserByEmail(User user) {

        redisTemplate.opsForValue().set(getUserKey(user.getEmail()), user);
    }

    public void setTokenByEmail(String token, String email) {

        redisTemplate.opsForValue().set(token, email, Duration.ofMinutes(10));

    }


    public User getUser(String email) {

        User user = (User) redisTemplate.opsForValue().get(getUserKey(email));

        if (user == null) {
            throw new RedisKeyNotFoundException(email);
        }

        return user;

    }

    public String getEmail(String token) {

        String email = (String) redisTemplate.opsForValue().get(token);

        if (email == null) {
            throw new RedisKeyNotFoundException(token);
        }

        return email;
    }


    private static String getUserKey(String email) {
        return USER_KEY + email;
    }




}
