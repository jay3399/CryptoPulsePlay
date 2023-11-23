package com.example.cryptopulseplay.application.ui.response;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class DuplicateMessageResponse implements SignInResponse{

    @Override
    public ResponseEntity<?> createResponse() {

        Map<String, String> messages = new HashMap<>();

        messages.put("message", "이미 인증요청을 보냈습니다 , 이메일을 확인해주세요");

        return ResponseEntity.ok(messages);


    }
}
