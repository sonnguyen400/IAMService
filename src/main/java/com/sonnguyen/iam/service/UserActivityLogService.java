package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.repository.UserActivityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserActivityLogService {
    UserActivityLogRepository userActivityLogRepository;
    HttpServletRequest request;

    public void log(UserActivityLog userActivityLog) {
        userActivityLogRepository.save(userActivityLog);
    }

    public List<UserActivityLog> getUserActivityLogs() {
        return userActivityLogRepository.findAll();
    }

    protected void saveActivityLog(UserActivityLog activityLog) {
        setActivityLogInfoFromRequest(activityLog);
        if (activityLog.getEmail() == null) {
            activityLog.setEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        }
        log(activityLog);
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
