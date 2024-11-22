package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.DuplicatedException;
import com.sonnguyen.iam.exception.InvalidArgumentException;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.repository.AccountRepository;
import com.sonnguyen.iam.utils.AbstractResponseMessage;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.utils.ResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessageStatus;
import com.sonnguyen.iam.viewmodel.AccountGetVm;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
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
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public AccountGetVm createNewAccount(AccountPostVm accountPostVm) {
        accountRepository.findByEmail(accountPostVm.email())
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
        return AccountGetVm.fromAccount(accountRepository.save(initialAccount));
    }

    public AbstractResponseMessage sendActiveAccountEmail(String email) throws Exception {
        accountRepository.findByEmail(email).orElseThrow(() -> new InvalidArgumentException(String.format("Email %s not found", email)));
        String activeCode = jwtUtils.generateToken(email, Instant.now().plus(3, ChronoUnit.MINUTES));
        log.info("Send code to email: {}", email);// Verification Code expired in 5 minutes
        log.info("Email content: http://localhost:8080/api/v1/account/active?code={}", activeCode);
        emailService.sendEmail(email, "Active account", "http://localhost:8080/api/v1/account/active?code=" + activeCode);
        return ResponseMessage.builder()
                        .message("An activation code was send to email"+email)
                        .status(ResponseMessageStatus.SUCCESS.status)
                .build();
    }

    @Transactional
    public AbstractResponseMessage verifyAccountByActiveCode(String activeCode) throws Exception {
        String extractedEmail = jwtUtils.validateToken(activeCode).getSubject();
        if (!accountRepository.existsByEmail(extractedEmail)) {
            throw new InvalidArgumentException("Invalid activation code");
        }
        accountRepository.enableAccountByEmail(extractedEmail);
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message(extractedEmail+" was verified")
                .build();
    }


    @Transactional
    public void changePassword(String email, String plainTextNewPassword) {
        String encodedPassword = argon2PasswordEncoder.encode(plainTextNewPassword);
        accountRepository.changePassword(email, encodedPassword);
    }

}
