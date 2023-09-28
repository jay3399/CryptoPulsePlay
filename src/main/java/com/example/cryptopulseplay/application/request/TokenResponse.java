package com.example.cryptopulseplay.application.request;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class TokenResponse implements SignInResponse {

    private final String loginToken;
    private final String refreshToken;

    public TokenResponse(String loginToken, String refreshToken) {
        this.loginToken = loginToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public ResponseEntity<?> createResponse() {

        Map<String, String> tokens = new HashMap<>();

        tokens.put("loginToken", loginToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);

    }
}
