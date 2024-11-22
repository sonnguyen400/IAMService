package com.sonnguyen.iam.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateResponseMessage extends AbstractResponseMessage{
    private String token;
    @Builder
    public AuthenticateResponseMessage(int status, String message, Object content, String token) {
        super(status, message, content);
        this.token = token;
    }

    public AuthenticateResponseMessage() {
        super();
    }
}
