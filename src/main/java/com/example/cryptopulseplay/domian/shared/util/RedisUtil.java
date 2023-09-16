package com.example.cryptopulseplay.domian.shared.util;

import com.example.cryptopulseplay.domian.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public final RedisTemplate redisTemplate;

    private static final String USER_KEY = "user:";

    public void setUserByEmail(String email, User user) {

        redisTemplate.opsForValue().set(getUserKey(email), user);
    }

    public void setTokenByEmail(String token, String email) {

        redisTemplate.opsForValue().set(token, email);

    }

    public User getUser(String email) {

        return (User) redisTemplate.opsForValue().get(getUserKey(email));

    }

    public String getEmail(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }


    private static String getUserKey(String email) {
        return USER_KEY + email;
    }




}
