package com.sonnguyen.iam.exception;

import com.sonnguyen.iam.utils.AbstractResponseMessage;
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

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getConstraintViolations().stream().map((ConstraintViolation::getMessage)).collect(Collectors.joining("\n")))
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleMethodArgumentException(MethodArgumentNotValidException e) {
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(Arrays.stream(e.getDetailMessageArguments()))
                .build();
    }
    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleDuplicatedException(DuplicatedException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleInvalidArgumentException(InvalidArgumentException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleInvalidArgumentException(BadCredentialsException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AbstractResponseMessage handleAuthenticationException(AuthenticationException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AbstractResponseMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.FAIL.status)
                .message(e.getMessage())
                .build();
    }
}
