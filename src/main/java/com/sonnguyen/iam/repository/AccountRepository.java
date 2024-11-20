package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("update Account a set a.isEnabled=true where a.email=?1")
    @Modifying
    void enableAccountByEmail(String email);
}
