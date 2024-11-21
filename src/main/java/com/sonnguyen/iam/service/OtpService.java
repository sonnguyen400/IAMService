package com.sonnguyen.iam.service;

import com.sonnguyen.iam.utils.OtpUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OtpService {
    Argon2PasswordEncoder passwordEncoder;
    RedisTemplate<Object,Object> redisTemplate;
    OtpUtils otpUtils;
    public String createAndSave(String subject) {
        String otp = otpUtils.generateOtp();
        redisTemplate.opsForValue().set(subject,passwordEncoder.encode(otp), Duration.of(3, ChronoUnit.MINUTES));
        return otp;
    }
    public boolean verifyOtp(String subject,String textPlainOtp) {
        String encodedOtp= (String) redisTemplate.opsForValue().get(subject);
        return passwordEncoder.matches(textPlainOtp,encodedOtp);
    }
}
