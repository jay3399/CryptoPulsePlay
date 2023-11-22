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


    /**
     * 회원가입 및 로그인
     * @param signInRequest 이메일 , 디바이스정보.
     * @return 로그인 토큰 발행.
     */
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {

        String email = signInRequest.getEmail();

        DeviceInfo deviceInfo = signInRequest.getDeviceInfo();

        User.DeviceInfo userDeviceInfo = deviceInfo.toDomain();

        SignInResponse signInResponse = userAppService.signIn(email, userDeviceInfo);

        return signInResponse.createResponse();
    }


    /**
     * 이메일 인증링크 엔드포인트
     * @param token 사용자 이메일 정보.
     * @return
     */
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
     * 필요없어짐.
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







    // 테스트 호출용  ----------------------------------------------------------------------------------------------------------------------------

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



    @GetMapping("/admin/test")
    public String getAdmin() {

        return "pass";

    }

   //----------------------------------------------------------------------------------------------------------------------------


}
