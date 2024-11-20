package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.service.AccountService;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;
    @PostMapping(value = "/register")
    public Account registerNewAccount(
             @Valid @RequestBody AccountPostVm account
    ) {
        return accountService.registerNewAccount(account);
    }
    @GetMapping(value = "/verify")
    public String verifyAccount(@RequestParam String email){
        accountService.sendActiveAccountEmail(email);
        return String.format("Send activation code to email %s",email);
    }
    @GetMapping(value = "/active")
    public String activeAccountByActiveCode(@RequestParam String code){
        accountService.verifyAccountByActiveCode(code);
        return "Active account successfully";
    }
}
