package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.viewmodel.AccountPostVm;
import com.sonnguyen.iam.viewmodel.ChangingPasswordPostVm;
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
    public ResponseEntity<String> login(@RequestBody AccountPostVm accountPostVm,HttpServletRequest request) throws Exception {
        saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).email(accountPostVm.email()).build());
        return authenticationService.login(accountPostVm);
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
