package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.AccountPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPermissionRepository extends JpaRepository<AccountPermission, Long> {
}
