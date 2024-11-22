package com.sonnguyen.iam.utils;

import lombok.Builder;

public class ResponseMessage extends AbstractResponseMessage{
    @Builder
    ResponseMessage(int status, Object message, Object content) {
        super(status, message, content);
    }
}
