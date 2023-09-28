package com.example.cryptopulseplay.domian.shared.util;

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

        return (User) redisTemplate.opsForValue().getAndDelete(getUserKey(email));

    }

    public String getEmail(String token) {
        return (String) redisTemplate.opsForValue().getAndDelete(token);
    }


    private static String getUserKey(String email) {
        return USER_KEY + email;
    }




}
