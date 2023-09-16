package com.example.cryptopulseplay.domian.shared.util;

import com.example.cryptopulseplay.domian.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public final RedisTemplate redisTemplate;

    public void setUserByEmail(String email, User user) {

        redisTemplate.opsForValue().set("user:" + email, user);
    }

    public void setTokenByEmail(String token, String email) {

        redisTemplate.opsForValue().set(token, email);

    }

    public User getUser(String email) {

        return (User) redisTemplate.opsForValue().get("user:" + email);

    }

    public String getEmail(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }




}
