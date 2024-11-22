package com.sonnguyen.iam.exception;

import com.sonnguyen.iam.utils.ResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessageStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getConstraintViolations().stream().map((ConstraintViolation::getMessage)).collect(Collectors.joining("\n")))
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleMethodArgumentException(MethodArgumentNotValidException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(Arrays.stream(e.getDetailMessageArguments()))
                .build();
    }
    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleDuplicatedException(DuplicatedException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({InvalidArgumentException.class, MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleInvalidArgumentException(Exception e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleInvalidArgumentException(BadCredentialsException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleAuthenticationException(AuthenticationException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }
}
