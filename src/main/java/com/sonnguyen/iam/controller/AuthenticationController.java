package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
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

}
