package com.example.cryptopulseplay.application.ui.controller;

import com.example.cryptopulseplay.application.exception.custom.JwtValidationException;
import com.example.cryptopulseplay.application.ui.request.PointRequest;
import com.example.cryptopulseplay.application.ui.request.SignInRequest;
import com.example.cryptopulseplay.application.ui.request.SignInRequest.DeviceInfo;
import com.example.cryptopulseplay.application.ui.response.SignInResponse;
import com.example.cryptopulseplay.application.service.UserAppService;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @PostMapping("/signIn")
    public ResponseEntity<?> signInOrUp(@Valid @RequestBody SignInRequest signInRequest) {

        String email = signInRequest.getEmail();

        DeviceInfo deviceInfo = signInRequest.getDeviceInfo();

        // 캡슐화 ++ 도메인 중심 . 상태 노출x getter x
        User.DeviceInfo userDeviceInfo = deviceInfo.toDomain();

//        User.DeviceInfo userDeviceInfo = new User.DeviceInfo(deviceInfo.getBrowser(), deviceInfo.getPlatform());

        SignInResponse signInResponse = userAppService.signInOrUp(email, userDeviceInfo);

        return signInResponse.createResponse();
    }


    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        try {
            jwtUtil.validateTokenForMail(token);
        } catch (JwtException e) {
            throw new JwtValidationException("JWT for Email Validation Exception", e);
        }

        String response = userAppService.verifyEmail(token);

        return ResponseEntity.ok(response);

    }


    /**
     * ?
     */
    @GetMapping("/verifyLoginToken")
    public ResponseEntity<String> verifyLoginToken(@RequestParam String token) {

        boolean isValid = jwtUtil.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok("valid token");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }


    }

    @PostMapping("/addPoint")
    public ResponseEntity<?> updatePoint(HttpServletRequest request,
            @RequestBody PointRequest pointRequest) {


        String token = JwtUtil.extractToken(request);

        Long userId = jwtUtil.getUserIdFromToken(token);

        userService.addPoint(userId, pointRequest.getPoint());

        return ResponseEntity.ok().body("complete");


    }

    @GetMapping("/currentUser")
    public String getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || !authentication.isAuthenticated()) {
            return "error";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        System.out.println("userDetails = " + userDetails.getUsername());

        return userDetails.getUsername();

    }
}
