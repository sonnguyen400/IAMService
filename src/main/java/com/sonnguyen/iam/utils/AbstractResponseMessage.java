package com.sonnguyen.iam.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractResponseMessage {
    private  int status;
    private  Object message;
    private  Object content;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(int status, Object message, Object content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }
}
