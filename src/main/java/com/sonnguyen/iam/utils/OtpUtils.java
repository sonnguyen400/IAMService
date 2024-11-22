package com.sonnguyen.iam.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OtpUtils {
    static final Random RANDOM = new SecureRandom();

    public String generateOtp() {
        return String.valueOf(RANDOM.nextInt(100000, 999999));
    }
}
