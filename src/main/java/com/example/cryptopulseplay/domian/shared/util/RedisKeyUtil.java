package com.example.cryptopulseplay.domian.shared.util;

public class RedisKeyUtil {

    private static final String USER_KEY = "user:";

    private static final String GAME_KEY = "game:";

    private static final String RECORD_KEY = "record";

    public static String getUserKey(String email) {
        return USER_KEY + email;
    }

    public static String getGameKey(Long userId) {
        return GAME_KEY + userId;
    }

    public static String getRecordKey() {
        return RECORD_KEY;
    }


}
