package com.example.cryptopulseplay.application.ui.response;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class TokenResponse implements SignInResponse {

    private final String loginToken;

    public TokenResponse(String loginToken) {
        this.loginToken = loginToken;
    }

    @Override
    public ResponseEntity<?> createResponse() {

        Map<String, String> tokens = new HashMap<>();

        tokens.put("loginToken", loginToken);

        return ResponseEntity.ok(tokens);

    }
}
