package com.example.cryptopulseplay.domian.shared.service;

import com.example.cryptopulseplay.domian.shared.JwtUtil;
import com.example.cryptopulseplay.domian.user.model.User;
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
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private static final String MAIL_ADDRESS = "CryptoPulsePLay";


    @Async
    public void sendVerificationEmail(String email, String token) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setFrom(MAIL_ADDRESS);
            helper.setFrom("이메일 인증을 완료해주세요");

            String htmlText = "<h3>Click the button below to verify your email:</h3>" +
                    "<a href='http://localhost:8080/verifyEmail?token=" + token + "'>" +
                    "<button style='padding: 10px; background-color: blue; color: white;'>Verify Email</button>"
                    +
                    "</a>";

            helper.setText(htmlText, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {

        }



    }











}
