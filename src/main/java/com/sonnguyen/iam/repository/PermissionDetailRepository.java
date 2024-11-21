package com.sonnguyen.iam.repository;

import com.sonnguyen.iam.model.PermissionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDetailRepository extends JpaRepository<PermissionDetail, Long> {
    @Query("SELECT pd from PermissionDetail pd where pd.permission_id in (select pa.permission_id from AccountPermission pa where pa.account_id=?1)")
    List<PermissionDetail> findAllPermissionDetailByAccountId(Long accountId);
}
