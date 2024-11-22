package com.sonnguyen.iam.utils;

import lombok.Builder;


public class AuthenticateResponseMessage extends AbstractResponseMessage{
    private final String token;
    @Builder
    public AuthenticateResponseMessage(int status, String message, Object content, String token) {
        super(status, message, content);
        this.token = token;
    }
}
