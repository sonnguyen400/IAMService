package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AccountPostVm accountPostVm){
        return authenticationService.login(accountPostVm);
    }

    @PostMapping(value = "/password/change")
    public String changePassword(@RequestBody ChangingPasswordPostVm changingPasswordPostVm){
        return authenticationService.changePassword(changingPasswordPostVm);
    }
}
