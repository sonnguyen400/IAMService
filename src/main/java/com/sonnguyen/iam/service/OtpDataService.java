package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.OtpData;
import com.sonnguyen.iam.repository.OtpDataRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OtpDataService {
    OtpDataRepository repository;

    public OtpData save(OtpData otpData) {
        Optional<OtpData> otpDataOptional = repository.findByEmail(otpData.getEmail(), Instant.now().getEpochSecond());
        otpDataOptional.ifPresent(data -> otpData.setId(data.getId()));
        return repository.save(otpData);
    }

    public Optional<OtpData> findByEmail(String email) {
        return repository.findByEmail(email, Instant.now().getEpochSecond());
    }
}
