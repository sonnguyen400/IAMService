package com.sonnguyen.iam.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage extends AbstractResponseMessage{
    public ResponseMessage() {
        super();
    }

    @Builder
    ResponseMessage(int status, Object message, Object content) {
        super(status, message, content);
    }
}
