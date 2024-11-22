package com.sonnguyen.iam.utils;

public enum ResponseMessageStatus {
    //Failure action
    FAIL(0),
    //Successful action
    SUCCESS(1),
    //Need complement action
    CONTINUOUS(2);
    public final int status;
    ResponseMessageStatus(int status) {
        this.status = status;
    }
}
