package com.example.cryptopulseplay.domian.shared.service;

import com.example.cryptopulseplay.application.exception.custom.MailSenderException;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import com.example.cryptopulseplay.domian.user.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    private static final String MAIL_ADDRESS = "josw90@naver.com";
    private static final String EMAIL_CHECK = "emailCheck";


    /**
     * MailException -> 스프링 JavaMail 의 최상위 예외  -> javaMailSender MessagingException -> JavaMail API
     * 발생예외 , 메세지 작성 및 전송중 발생 0 -> MimeMessageHelper
     */
    @Async
    public void sendVerificationEmail(User user) {

        // 이메일검증용 토큰
        String token = jwtUtil.generateToken(user.getEmail(), EMAIL_CHECK);

        // 토큰값 매칭 이메일 레디스에 저장 .

        redisUtil.setTokenByEmail(token, user.getEmail());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            System.out.println("user.getEmail() = " + user.getEmail());
            helper.setTo(user.getEmail());
            helper.setFrom(MAIL_ADDRESS);
            helper.setSubject("이메일 인증을 완료해주세요");

            String htmlText = "<h3>Click the button below to verify your email:</h3>" +
                    "<a href='http://localhost:8080/verifyEmail?token=" + token + "'>" +
                    "<button style='padding: 10px; background-color: blue; color: white;'>Verify Email</button>"
                    +
                    "</a>";

            helper.setText(htmlText, true);

            javaMailSender.send(mimeMessage);

            System.out.println("email = " + user.getEmail());

        } catch (MessagingException e) {
            throw new MailSenderException("message create failed", e);
        } catch (MailException e) {
            throw new MailSenderException("mail sending failed", e);
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

//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final RedisTemplate redisTemplate;


}
