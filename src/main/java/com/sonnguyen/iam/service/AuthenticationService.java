package com.sonnguyen.iam.service;

import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {
    public static String authCookie = "token";
    AuthenticationManager authenticationManager;
    AccountService accountService;
    JWTUtils jwtUtils;
    public ResponseEntity<String> login(AccountPostVm accountPostVm) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(accountPostVm.email(), accountPostVm.password());
        Authentication authenticatedAuth=authenticationManager.authenticate(authentication);
        if(authenticatedAuth==null||!authenticatedAuth.isAuthenticated()) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return handleLoginSuccess(authenticatedAuth);
    }
    public ResponseEntity<String> handleLoginSuccess(Authentication authentication) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,generateAccessCookie(authentication).toString())
                .body("login successfully");
    }
    public ResponseCookie generateAccessCookie(Authentication authentication){
        return ResponseCookie.from(authCookie,generateJwtAccessToken(authentication))
                .secure(true)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .maxAge(60*60*3)
                .build();
    }
    public String generateJwtAccessToken(Authentication authentication){
        List<String> authoritiesName=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Map<String,Object> claims=Map.of("authorities",authoritiesName,"principal",authentication.getPrincipal());
        return jwtUtils.generateToken(claims,authentication.getName(),(new Date().getTime()+1000*60*60*3));
    }
    public String changePassword(ChangingPasswordPostVm changingPasswordPostVm){
        Authentication authentication = new UsernamePasswordAuthenticationToken(changingPasswordPostVm.email(), changingPasswordPostVm.oldPassword());
        Authentication authenticatedAuth=authenticationManager.authenticate(authentication);
        if(authenticatedAuth==null||!authenticatedAuth.isAuthenticated()) {
            throw new BadCredentialsException("Invalid email or password");
        }
        accountService.changePassword(changingPasswordPostVm.email(), changingPasswordPostVm.newPassword());
        return "Change password successfully";
    }
}
