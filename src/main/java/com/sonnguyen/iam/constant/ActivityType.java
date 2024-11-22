package com.sonnguyen.iam.constant;

public enum ActivityType {
    LOGIN(1),
    LOGOUT(2),
    MODIFY_PASSWORD(3),
    MODIFY_PROFILE(4);
    public final int value;
    ActivityType(int value) {
        this.value = value;
    }

}
