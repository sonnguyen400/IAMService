package com.sonnguyen.iam.service;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.model.UserDetailsImpl;
import com.sonnguyen.iam.security.AuthenticationManagement;
import com.sonnguyen.iam.utils.*;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import com.sonnguyen.iam.viewmodel.ForgotPasswordPostVm;
import com.sonnguyen.iam.viewmodel.LoginAcceptRequestVm;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    public static String ACCESS_TOKEN = "token";
    public static String REFRESH_TOKEN = "refreshtoken";
    AbstractEmailService mailService;
    AuthenticationManagement authenticationManager;
    AccountService accountService;
    OtpService otpService;
    ForbiddenTokenService forbiddenTokenService;
    UserActivityLogService userActivityLogService;
    UserDetailsServiceImpl userDetailsService;
    JWTUtils jwtUtils;

    public AbstractResponseMessage handleLoginRequest(AccountPostVm accountPostVm) throws Exception {
        Authentication authenticatedAuth = authenticationManager.authenticate(accountPostVm.email(), accountPostVm.password());
        return handleSuccessLoginRequest(authenticatedAuth);
    }

    public AbstractResponseMessage handleSuccessLoginRequest(Authentication authenticatedAuth) throws Exception {
        String otp = otpService.createAndSave(authenticatedAuth.getPrincipal().toString());
        Map<String, Object> claims = Map.of(
                "principal", authenticatedAuth.getPrincipal(),
                "scope", authenticatedAuth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "))
        );
        mailService.sendEmail((String) authenticatedAuth.getPrincipal(), "OTP for validate login session", otp);
        log.info("A valid OTP {} was sent to email:{}", otp,authenticatedAuth.getPrincipal());
        String temporaryJwt=jwtUtils.generateToken(claims, "", Instant.now().plus(Duration.ofMinutes(3)));
        return AuthenticateResponseMessage
                .builder()
                .token(temporaryJwt)
                .status(ResponseMessageStatus.SUCCESS.status)
                .message("Send received token along OTP to login")
                .build();
    }

    public ResponseEntity<Map<String,Object>> handleLoginAcceptRequest(LoginAcceptRequestVm loginAcceptRequestVm) throws Exception {
        Claims claims = jwtUtils.validateToken(loginAcceptRequestVm.token());
        String subject = claims.get("principal", String.class);
        String scope = claims.get("scope", String.class);
        boolean isValidOtp = otpService.verifyOtp(subject, loginAcceptRequestVm.otp());
        if (isValidOtp) {
            mailService.sendEmail(subject,"Login Successfully","Login");
            userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).email(subject).build());
            return handleLoginSuccess(subject, scope);
        }
        throw new AuthenticationException("Invalid OTP");
    }

    public ResponseEntity<Map<String,Object>> handleLoginSuccess(String subject, String scope) throws Exception {
        ResponseCookie accessCookie=generateAccessCookie(subject,scope);
        Map<String,Object> claims=Map.of("principal",subject);
        String refreshToken=jwtUtils.generateToken(claims,"",Instant.now().plus(Duration.ofDays(7)));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(Map.of(REFRESH_TOKEN,refreshToken));
    }

    public ResponseCookie generateAccessCookie(String subject, String scope) throws Exception {
        Map<String, Object> claims = Map.of("scope", scope);
        String token = jwtUtils.generateToken(claims, subject, Instant.now().plus(3, ChronoUnit.HOURS));
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .secure(true)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .maxAge(Duration.ofHours(3))
                .build();
    }


    public AbstractResponseMessage changePassword(ChangingPasswordPostVm changingPasswordPostVm) {
        Authentication authentication = authenticationManager.authenticate(changingPasswordPostVm.email(), changingPasswordPostVm.oldPassword());
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid email or password");
        }
        mailService.sendEmail(changingPasswordPostVm.email(), "Update Credentials", "Your password has been changed");
        accountService.changePassword(changingPasswordPostVm.email(), changingPasswordPostVm.newPassword());
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PASSWORD).build());
        return ResponseMessage
                .builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message("Change password successfully")
                .build();
    }

    public ResponseEntity<String> logout(HttpServletRequest request) {
        String accessToken = RequestUtils.extractValueFromCookie(request, ACCESS_TOKEN);
        String refreshToken = RequestUtils.extractValueFromCookie(request, REFRESH_TOKEN);
        if (accessToken != null) {
            forbiddenTokenService.saveAll(accessToken,refreshToken);
        }
        ResponseCookie expiredCookie = ResponseCookie
                .from(ACCESS_TOKEN, "Expired")
                .maxAge(Duration.ofSeconds(0))
                .build();
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGOUT).build());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body("Logout Success");
    }

    public ResponseMessage sendEmailForgotPassword(String email) throws Exception {
        accountService.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Can't not find account registered with "+email));
        String token=jwtUtils.generateToken(email,Instant.now().plus(5, ChronoUnit.MINUTES));
        log.info("Email was sent: {}","http://localhost:8080/api/v1/auth/password/forgot/accept?token="+token);
        mailService.sendEmail(email,"Forgot password","http://localhost:8080/api/v1/auth/password/forgot/accept?token="+token);
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message("An authentication email was sent to your email address : ")
                .content("[Debug]:"+token)
                .build();
    }
    public ResponseMessage acceptForgotPasswordRequest(ForgotPasswordPostVm forgotPasswordPostVm) throws Exception {
        String email=jwtUtils.validateToken(forgotPasswordPostVm.token()).getSubject();
        accountService.changePassword(email,forgotPasswordPostVm.newPassword());
        mailService.sendEmail(email,"Update password","Your password has been changed");
        return  ResponseMessage.builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message("Change password successfully")
                .build();
    }


    public ResponseEntity<Map<String,Object>> handleRefreshTokenRequest(HttpServletRequest request) throws Exception {
        // Validate Refresh Token
        String refreshToken=request.getHeader(REFRESH_TOKEN);
        if(forbiddenTokenService.existsByToken(refreshToken)) throw new AuthenticationException("Invalid refresh token");
        String principal=jwtUtils.validateToken(refreshToken).get("principal",String.class);

        // Generate Access token
        UserDetailsImpl userDetails=userDetailsService.loadUserByUsername(principal);
        String scope=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        ResponseCookie newAccessCookie=generateAccessCookie(userDetails.getEmail(),scope);

        // Generate Refresh token
        Map<String,Object> claims=Map.of("principal",principal);
        String newRefreshToken=jwtUtils.generateToken(claims,"",Instant.now().plus(Duration.ofDays(7)));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                .body(Map.of(REFRESH_TOKEN,refreshToken));
    }
}
