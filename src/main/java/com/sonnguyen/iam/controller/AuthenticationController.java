package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.utils.AbstractResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessage;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import com.sonnguyen.iam.viewmodel.ForgotPasswordPostVm;
import com.sonnguyen.iam.viewmodel.LoginAcceptRequestVm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public AbstractResponseMessage handleLoginRequest(@RequestBody AccountPostVm accountPostVm, HttpServletRequest request) throws Exception {
        return authenticationService.handleLoginRequest(accountPostVm);
    }
    @PostMapping(value = "/login/verify")
    public ResponseEntity<ResponseMessage> handleLoginAcceptRequest(@RequestBody LoginAcceptRequestVm loginAcceptRequestVm) throws Exception {
        return authenticationService.handleLoginAcceptRequest(loginAcceptRequestVm);
    }
    @GetMapping(value = "/login/refresh")
    public ResponseEntity<ResponseMessage> handleRefreshToken(HttpServletRequest request) throws Exception {
        return authenticationService.handleRefreshTokenRequest(request);
    }
    @PostMapping(value = "/password/change")
    @PreAuthorize("hasAnyAuthority('CHANGE_USER_PASSWORD') or (hasAnyAuthority('CHANGE_PASSWORD') and #changingPasswordPostVm.email()==authentication.principal)")
    public AbstractResponseMessage changePassword(@RequestBody ChangingPasswordPostVm changingPasswordPostVm) throws Exception {
        return authenticationService.changePassword(changingPasswordPostVm);
    }
    @GetMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {
        return authenticationService.logout(request);
    }

    @GetMapping(value = "/password/forgot")
    public ResponseMessage requestForgotPassword(@RequestParam String email) throws Exception {
        return authenticationService.sendEmailForgotPassword(email);
    }
    @PostMapping(value = "/password/forgot/accept")
    public ResponseMessage forgotPasswordAccept(@RequestBody @Valid ForgotPasswordPostVm forgotPasswordPostVm) throws Exception {
        return authenticationService.acceptForgotPasswordRequest(forgotPasswordPostVm);
    }
}
