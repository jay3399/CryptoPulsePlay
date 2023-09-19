package com.example.cryptopulseplay.application.controller;

import com.example.cryptopulseplay.application.request.EmailValidRequest;
import com.example.cryptopulseplay.application.service.UserAppService;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final JwtUtil jwtUtil;


    @PostMapping("/signIn")
    public ResponseEntity<Map<String, String>> signInOrUp(@Valid @RequestBody EmailValidRequest emailValidRequest, @RequestHeader("DeviceInfo") String device) {

        System.out.println("emailValidRequest = " + emailValidRequest);

        String email = emailValidRequest.getEmail();

        Map<String, String> tokens = userAppService.signInOrUp(email, device);

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String response = userAppService.verifyEmail(token);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/verifyLoginToken")
    public ResponseEntity<String> verifyLoginToken(@RequestParam String token) {

        boolean isValid = jwtUtil.validateToken(token);

        System.out.println("isValid = " + isValid);

        if (isValid) {
            return ResponseEntity.ok("valid token");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }



    }












}
