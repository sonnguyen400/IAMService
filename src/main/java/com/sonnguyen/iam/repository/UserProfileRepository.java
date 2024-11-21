package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    @Query("select u from UserProfile u where u.account_id=?1")
    Optional<UserProfile> findByAccount_id(Long id);
}
