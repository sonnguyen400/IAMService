package com.sonnguyen.iam.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream().map((ConstraintViolation::getMessage)).collect(Collectors.joining("\n"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentException(MethodArgumentNotValidException e) {
        return Arrays.stream(e.getDetailMessageArguments());
    }
    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicatedException(DuplicatedException e) {
        return e.getMessage();
    }
    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidArgumentException(InvalidArgumentException e) {
        return e.getMessage();
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidArgumentException(BadCredentialsException e) {
        return e.getMessage();
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAuthenticationException(AuthenticationException e) {
        return e.getMessage();
    }
}
