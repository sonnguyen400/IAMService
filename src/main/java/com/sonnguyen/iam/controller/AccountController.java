package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.service.AccountService;
import com.sonnguyen.iam.service.UserAccountService;
import com.sonnguyen.iam.utils.AbstractResponseMessage;
import com.sonnguyen.iam.viewmodel.AccountGetVm;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;
    UserAccountService userAccountService;

    @PostMapping(value = "/register")
    public AbstractResponseMessage registerNewAccount(
            @Valid @RequestBody AccountPostVm account
    ) {
        return userAccountService.registerNewAccount(account);
    }

    @GetMapping(value = "/verify")
    public AbstractResponseMessage verifyAccount(@RequestParam String email) throws Exception {
        return accountService.sendActiveAccountEmail(email);
    }

    @GetMapping(value = "/active")
    public AbstractResponseMessage activeAccountByActiveCode(@RequestParam String code) throws Exception {
        return accountService.verifyAccountByActiveCode(code);
    }

    @GetMapping(value = "/{id}")
    public AccountGetVm getAccountById(@PathVariable Long id) {
        return accountService.findById(id);
    }
}
