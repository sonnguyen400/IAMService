package com.sonnguyen.iam.utils;

public abstract class AbstractResponseMessage {
    private final int status;
    private final Object message;
    private final Object content;

    public AbstractResponseMessage(int status, Object message, Object content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }
}
