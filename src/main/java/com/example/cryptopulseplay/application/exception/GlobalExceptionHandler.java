package com.example.cryptopulseplay.application.exception;


import com.example.cryptopulseplay.application.exception.custom.MailSenderException;
import com.example.cryptopulseplay.application.exception.custom.MailVerificationException;
import com.example.cryptopulseplay.application.exception.custom.RedisKeyNotFoundException;
import com.example.cryptopulseplay.application.ui.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisConnectionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 필요예외 -
 *
 * 디비 , 제약조건 위반 o  , null
 * 레디스 ,연결  , null o
 * 컨트롤러 검증 , valid x
 * 이메일요청 o
 * 이메일 검증 o
 * jwt o
 *
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        String message = "Entity not found";
        log.error(message, e);
        return createErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(RedisConnectionException.class)
    public ResponseEntity<ErrorResponse> handleRedisConnectionException(RedisConnectionException e) {
        String message ="Redis connection error";
        log.error(message, e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleEmailException(MailException e) {
        String message = "email error";
        log.error(message, e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        String message = "jwt error";
        log.error(message, e);
        return createErrorResponse(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintException(ConstraintViolationException e) {
        String message = "DB constraint error";
        log.error(message, e);
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }




    // Custom Error --------------------------------------------------------------------------------

    @ExceptionHandler(RedisKeyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRedisKeyNotFoundException(RedisKeyNotFoundException e) {

        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MailVerificationException.class)
    public ResponseEntity<ErrorResponse> handleMailVerificationException(
            MailVerificationException e) {

        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MailSenderException.class)
    public ResponseEntity<ErrorResponse> handleMailSenderException(MailSenderException e) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {

        ErrorResponse errorResponse = ErrorResponse.create(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

}
