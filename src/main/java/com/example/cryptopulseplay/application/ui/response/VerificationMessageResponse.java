package com.example.cryptopulseplay.application.ui.response;

import com.example.cryptopulseplay.domian.user.model.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class VerificationMessageResponse implements SignInResponse {

    private User user;

    public VerificationMessageResponse(User user) {
        this.user = user;
    }

    @Override
    public ResponseEntity<?> createResponse() {

        Map<String, String> messages = new HashMap<>();

        if (!user.isEmailVerified()) {
            messages.put("message", "환영합니다, 가입인증 링크를 보냈습니다 이메일을 확인해주세요");
        }

        messages.put("message", "로그인 인증 링크를 보냈습니다 , 이메일을 확인해주세요");

        return ResponseEntity.ok(messages);

    }
}
