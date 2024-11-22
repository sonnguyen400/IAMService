package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.OtpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpDataRepository extends JpaRepository<OtpData, Integer> {
    @Query("Select otp from OtpData otp where otp.email=?1 and otp.expiredTimeSeconds>=2")
    Optional<OtpData> findByEmail(@NonNull String email, @NonNull Long expiredTimeSeconds);
}
