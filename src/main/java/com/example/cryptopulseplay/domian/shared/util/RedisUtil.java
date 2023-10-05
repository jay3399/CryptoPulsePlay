package com.example.cryptopulseplay.domian.shared.util;

import static com.example.cryptopulseplay.domian.shared.util.RedisKeyUtil.getGameKey;
import static com.example.cryptopulseplay.domian.shared.util.RedisKeyUtil.getUserKey;

import com.example.cryptopulseplay.application.exception.custom.RedisKeyNotFoundException;
import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.user.model.User;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public final RedisTemplate redisTemplate;

    // login , verification -----------------------------------------------------------------------------------------------------------------------


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

    // Game-----------------------------------------------------------------------------------------------------------------------

    // + 중복검사

    public void setGameByUser(Long userId, Game game) {

        redisTemplate.opsForValue().set(getGameKey(userId.toString()), game);

    }

    public Game getGame(String gameKey) {
        return (Game) redisTemplate.opsForValue().getAndDelete(gameKey);
    }

    public void setPriceRecord(PriceRecord priceRecord) {
        redisTemplate.opsForValue().set(RedisKeyUtil.getRecordKey(), priceRecord);
    }

    public PriceRecord getPriceRecord() {
        return  (PriceRecord) redisTemplate.opsForValue().getAndDelete(RedisKeyUtil.getRecordKey());
    }

    public Set<String> gameKeys() {
        return redisTemplate.keys(RedisKeyUtil.getGameKey("*"));
    }


    public void deleteGame(String gameKey) {

        redisTemplate.delete(gameKey);

    }



}
