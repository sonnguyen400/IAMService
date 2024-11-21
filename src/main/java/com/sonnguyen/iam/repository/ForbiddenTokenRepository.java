package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.ForbiddenToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForbiddenTokenRepository extends JpaRepository<ForbiddenToken, Long> {
    boolean existsByToken(String token);
}
