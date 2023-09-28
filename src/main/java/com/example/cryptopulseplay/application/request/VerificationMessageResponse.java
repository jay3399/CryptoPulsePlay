package com.example.cryptopulseplay.application.request;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class VerificationMessageResponse implements SignInResponse {


    @Override
    public ResponseEntity<?> createResponse() {

        Map<String, String> messages = new HashMap<>();

        messages.put("message", "이메일인증이 필요합니다");

        return ResponseEntity.ok(messages);

    }
}
