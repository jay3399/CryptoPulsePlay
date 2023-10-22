package com.example.cryptopulseplay.application.exception;


import com.example.cryptopulseplay.application.exception.custom.AlreadyParticipatingException;
import com.example.cryptopulseplay.application.exception.custom.InsufficientPointsException;
import com.example.cryptopulseplay.application.exception.custom.JwtValidationException;
import com.example.cryptopulseplay.application.exception.custom.MailSenderException;
import com.example.cryptopulseplay.application.exception.custom.MailVerificationException;
import com.example.cryptopulseplay.application.exception.custom.RedisKeyNotFoundException;
import com.example.cryptopulseplay.application.ui.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisConnectionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
    }

    @ExceptionHandler(RedisConnectionException.class)
    public ResponseEntity<ErrorResponse> handleRedisConnectionException(
            RedisConnectionException e) {
        String message = "Redis connection error";
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

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(TimeoutException e) {
        String message = "SSE connection timeout";
        log.error(message);
        return createErrorResponse(HttpStatus.REQUEST_TIMEOUT, message);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        String message = "IO Exception";
        log.error(message);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(
                        Collectors.joining(","));
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);

    }

    // Custom Error --------------------------------------------------------------------------------

    @ExceptionHandler(RedisKeyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRedisKeyNotFoundException(
            RedisKeyNotFoundException e) {

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

    @ExceptionHandler(InsufficientPointsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPointsException(InsufficientPointsException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());

    }

    @ExceptionHandler(AlreadyParticipatingException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyParticipationException(
            AlreadyParticipatingException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ErrorResponse> handleJwtValidationException(JwtValidationException e) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {

        ErrorResponse errorResponse = ErrorResponse.create(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

}
