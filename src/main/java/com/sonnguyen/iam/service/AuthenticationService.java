package com.sonnguyen.iam.service;

import com.sonnguyen.iam.security.AuthenticationManagement;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {
    public static String authCookie = "token";
    AuthenticationManagement authenticationManager;
    JWTUtils jwtUtils;
    public ResponseEntity<String> login(AccountPostVm accountPostVm) {
        Authentication authenticatedAuth=authenticationManager.authenticate(accountPostVm.email(), accountPostVm.password());
        return handleLoginSuccess(authenticatedAuth);
    }
    public ResponseEntity<String> handleLoginSuccess(Authentication authentication) {
        System.out.println(authentication.getName());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,generateAccessCookie(authentication.getName()).toString())
                .body(authentication.getName());
    }
    public ResponseCookie generateAccessCookie(String subject){
        return ResponseCookie.from(authCookie,jwtUtils.generateToken(subject))
                .secure(true)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .maxAge(60*60*3)
                .build();
    }
}
