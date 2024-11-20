package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.DuplicatedException;
import com.sonnguyen.iam.exception.InvalidArgumentException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.repository.AccountRepository;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    AccountRepository accountRepository;
    Argon2PasswordEncoder argon2PasswordEncoder;
    AbstractEmailService emailService;
    JWTUtils jwtUtils;
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
    public void sendActiveAccountEmail(String email) {
        accountRepository.findByEmail(email).orElseThrow(()->new InvalidArgumentException(String.format("Email %s not found", email)));
        String activeCode=jwtUtils.generateToken(email,(new Date().getTime()+1000*60*5));
        log.info("Send code to email: {}", email);// Verification Code expired in 5 minutes
        log.info("Email content: http://localhost:8080/api/v1/account/active?code={}", activeCode);
        emailService.sendEmail(email,"Active account","http://localhost:8080/api/v1/account/active?code="+activeCode);
    }
    @Transactional
    public void verifyAccountByActiveCode(String activeCode) {
        if(jwtUtils.isTokenExpired(activeCode)) {
            throw new InvalidArgumentException(String.format("Activation code %s is expired", activeCode));
        }
        String extractedEmail = jwtUtils.extractSubject(activeCode);
        log.info("Extracted email: "+extractedEmail);
        if(!accountRepository.existsByEmail(extractedEmail)) {
            throw new InvalidArgumentException("Invalid activation code");
        }
        accountRepository.enableAccountByEmail(extractedEmail);
    }
    public void updateAccount(Account account) {

    }

}
