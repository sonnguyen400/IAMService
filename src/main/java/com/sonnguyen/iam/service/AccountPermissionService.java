package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.AccountPermission;
import com.sonnguyen.iam.repository.AccountPermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountPermissionService {
    AccountPermissionRepository accountPermissionRepository;
    public AccountPermission save(AccountPermission accountPermission) {
        return accountPermissionRepository.save(accountPermission);
    }
}
