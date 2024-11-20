package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.DuplicatedException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.repository.AccountRepository;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    Argon2PasswordEncoder argon2PasswordEncoder;
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
    public Account registerNewAccount(  AccountPostVm accountPostVm) {
        findByEmail(accountPostVm.email())
                .ifPresent((duplicatedAccount) -> {
                    throw new DuplicatedException(String.format("Duplicated email %s", duplicatedAccount.getEmail()));
                });
        Account account = new Account();
        account.setEmail(accountPostVm.email());
        account.setIsEnabled(false);
        account.setPassword(argon2PasswordEncoder.encode(accountPostVm.password()));
        return accountRepository.save(account);
    }
    public void updateAccount(Account account) {

    }

}
