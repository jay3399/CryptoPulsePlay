package com.example.cryptopulseplay.application.controller;

import com.example.cryptopulseplay.application.request.EmailValidRequest;
import com.example.cryptopulseplay.application.service.UserAppService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;


    @PostMapping("/signIn")
    public ResponseEntity<Map<String, String>> signInOrUp(
            @Valid @RequestBody EmailValidRequest emailValidRequest,
            @RequestHeader("DeviceInfo") String device) {

        String email = emailValidRequest.getEmail();

        Map<String, String> tokens = userAppService.signInOrUp(email, device);

        return ResponseEntity.ok(tokens);
    }


    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String response = userAppService.verifyEmail(token);

        return ResponseEntity.ok(response);

    }












}
