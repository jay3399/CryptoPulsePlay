package com.example.cryptopulseplay.application.ui.response;

import org.springframework.http.ResponseEntity;


// 로그인 반환 , 인증이 필요한사람의 경우는 메세지 response , 인증이 필요없는경우는 tokenResponse를 반환
public interface SignInResponse {

    ResponseEntity<?> createResponse();


}
