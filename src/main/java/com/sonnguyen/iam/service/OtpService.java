package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.AuthenticationException;
import com.sonnguyen.iam.model.OtpData;
import com.sonnguyen.iam.utils.OtpUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OtpService {
    Argon2PasswordEncoder passwordEncoder;
    OtpDataService otpDataService;
    OtpUtils otpUtils;

    public String createAndSave(String subject) {
        String otp = otpUtils.generateOtp();
        OtpData otpData = new OtpData();
        otpData.setOtp(passwordEncoder.encode(otp));
        otpData.setActive(true);
        otpData.setEmail(subject);
        otpData.setExpiredTimeSeconds(Instant.now().plus(Duration.ofMinutes(5)).getEpochSecond());
        otpDataService.save(otpData);
        return otp;
    }

    public boolean verifyOtp(String subject, String textPlainOtp) {
        Optional<OtpData> optionalOtpData = otpDataService.findByEmail(subject);
        optionalOtpData.orElseThrow(() -> new AuthenticationException("Invalid OTP"));
        return passwordEncoder.matches(textPlainOtp, optionalOtpData.get().getOtp());
    }
}
