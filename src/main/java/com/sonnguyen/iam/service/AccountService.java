package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.DuplicatedException;
import com.sonnguyen.iam.exception.InvalidArgumentException;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.repository.AccountRepository;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.viewmodel.AccountGetVm;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public AccountGetVm findById(Long id) {
        return accountRepository
                .findById(id)
                .map(AccountGetVm::fromAccount)
                .orElseThrow(()->new ResourceNotFoundException("Resource not found"));
    }
    public AccountGetVm registerNewAccount(AccountPostVm accountPostVm) {
        findByEmail(accountPostVm.email())
                .ifPresent((duplicatedAccount) -> {
                    throw new DuplicatedException(String.format("Duplicated email %s", duplicatedAccount.getEmail()));
                });
        Account initialAccount = Account
                .builder()
                .email(accountPostVm.email())
                .isEnabled(false)
                .password(argon2PasswordEncoder.encode(accountPostVm.password()))
                .consecutiveLoginFailures(0)
                .build();
        return AccountGetVm.fromAccount( accountRepository.save(initialAccount));
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
