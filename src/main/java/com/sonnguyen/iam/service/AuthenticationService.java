package com.sonnguyen.iam.service;

import com.sonnguyen.iam.security.AuthenticationManagement;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.utils.RequestUtils;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import jakarta.servlet.http.HttpServletRequest;
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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {
    public static String authCookie = "token";
    AuthenticationManagement authenticationManager;
    AccountService accountService;
    ForbiddenTokenService forbiddenTokenService;
    JWTUtils jwtUtils;
    public ResponseEntity<String> login(AccountPostVm accountPostVm) throws Exception {
        Authentication authenticatedAuth=authenticationManager.authenticate(accountPostVm.email(), accountPostVm.password());
        return handleLoginSuccess(authenticatedAuth);
    }
    public ResponseEntity<String> handleLoginSuccess(Authentication authentication) throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,generateAccessCookie(authentication).toString())
                .body("login successfully");
    }
    public ResponseCookie generateAccessCookie(Authentication authentication) throws Exception {
        return ResponseCookie.from(authCookie,generateJwtAccessToken(authentication))
                .secure(true)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .maxAge(Duration.of(3, ChronoUnit.HOURS))
                .build();
    }
    public String generateJwtAccessToken(Authentication authentication) throws Exception {
        String scope=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        Map<String,Object> claims=Map.of("scope",scope);
        return jwtUtils.generateToken(claims, (String) authentication.getPrincipal(), Instant.now().plus(3, ChronoUnit.HOURS));
    }
    public String changePassword(ChangingPasswordPostVm changingPasswordPostVm){
        Authentication authentication = authenticationManager.authenticate(changingPasswordPostVm.email(), changingPasswordPostVm.oldPassword());
        if(authentication==null||!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid email or password");
        }
        accountService.changePassword(changingPasswordPostVm.email(), changingPasswordPostVm.newPassword());
        return "Change password successfully";
    }
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token= RequestUtils.extractValueFromCookie(request,authCookie);
        if(token!=null){
            forbiddenTokenService.save(token);
        }
        ResponseCookie expiredCookie=ResponseCookie
                .from(authCookie,"Expired")
                .maxAge(Duration.of(1,ChronoUnit.SECONDS))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,expiredCookie.toString())
                .body("Logout Success");
    }
}
