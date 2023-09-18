package com.example.cryptopulseplay.domian.shared.service;

import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String MAIL_ADDRESS = "josw90@naver.com";

//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final RedisTemplate redisTemplate;



    public void sendVerificationEmail(String email, String token) {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setFrom(MAIL_ADDRESS);
            helper.setSubject("이메일 인증을 완료해주세요");

            String htmlText = "<h3>Click the button below to verify your email:</h3>" +
                    "<a href='http://localhost:8080/verifyEmail?token=" + token + "'>" +
                    "<button style='padding: 10px; background-color: blue; color: white;'>Verify Email</button>"
                    +
                    "</a>";

            helper.setText(htmlText, true);

            javaMailSender.send(mimeMessage);

            System.out.println("email = " + email);

        } catch (MessagingException e) {

            System.out.println("오류");

        }



    }


//    public String verifyEmail(String token) {
//
//        String email = jwtUtil.validateToken(token).getSubject();
//
//        User user = redisUtil.getUser(email);
//
//        String emailInRedis = redisUtil.getEmail(token);
//
//        if (emailInRedis == null || !email.equals(email)) {
//            return null;
//            //예외처리
//        }
//
//        if (!user.isEmailVerified()) {
//
//            user.markEmailAsVerified(user, jwtUtil.generateRefreshToken(user));
//
//            userRepository.save(user);
//        }
//
//        return jwtUtil.generateToken(user, "loginCheck");
//
//    }



}
