package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
import com.sonnguyen.iam.viewmodel.LoginAcceptRequestVm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.AbstractController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {
    AuthenticationService authenticationService;
    @PostMapping(value = "/login")
    public ResponseEntity<String> handleLoginRequest(@RequestBody AccountPostVm accountPostVm,HttpServletRequest request) throws Exception {
        saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).email(accountPostVm.email()).build());
        return authenticationService.handleLoginRequest(accountPostVm);
    }
    @PostMapping(value = "/login/verify")
    public ResponseEntity<String> handleLoginAcceptRequest(@RequestBody LoginAcceptRequestVm loginAcceptRequestVm) throws Exception {
        return authenticationService.handleLoginAcceptRequest(loginAcceptRequestVm);
    }
    @PostMapping(value = "/password/change")
    @PreAuthorize("hasAnyAuthority('CHANGE_USER_PASSWORD') or (hasAnyAuthority('CHANGE_PASSWORD') and #changingPasswordPostVm.email()==authentication.principal)")
    public String changePassword(@RequestBody ChangingPasswordPostVm changingPasswordPostVm) throws Exception {
        saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PASSWORD).build());
        return authenticationService.changePassword(changingPasswordPostVm);
    }
    @GetMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {
        saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGOUT).build());
        return authenticationService.logout(request);
    }
}
