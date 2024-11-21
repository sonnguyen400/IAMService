package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.service.UserActivityLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseController {
    @Autowired
    UserActivityLogService activityLogService;
    @Autowired
    HttpServletRequest request;
    protected void saveActivityLog(UserActivityLog activityLog) {
        setActivityLogInfoFromRequest(activityLog);
        if(activityLog.getEmail()==null){
            activityLog.setEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        }
        activityLogService.log(activityLog);
    }
    private void setActivityLogInfoFromRequest(UserActivityLog activityLog) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        activityLog.setIpAddress(ipAddress);
        activityLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
    }
}
