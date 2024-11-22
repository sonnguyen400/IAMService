package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.AccountPermission;
import com.sonnguyen.iam.model.Permission;
import com.sonnguyen.iam.utils.AbstractResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessageStatus;
import com.sonnguyen.iam.viewmodel.AccountGetVm;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class UserAccountService {
    AccountService accountService;
    PermissionService permissionService;
    AccountPermissionService accountPermissionService;

    @Transactional
    public AbstractResponseMessage registerNewAccount(AccountPostVm accountPostVm) {
        //Find appropriate permission for new Account
        Permission permission = permissionService.findByName("USER").orElseThrow(
                () -> new RuntimeException("Permission not found")
        );
        //Init account
        AccountGetVm account = accountService.createNewAccount(accountPostVm);
        // Init permission
        AccountPermission accountPermission = AccountPermission.builder()
                .permission_id(permission.getId())
                .account_id(account.id())
                .build();
        accountPermissionService.save(accountPermission);
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.CONTINUOUS.status)
                .content("")
                .message("Account is registered successfully but still need to be verify")
                .build();
    }
}
