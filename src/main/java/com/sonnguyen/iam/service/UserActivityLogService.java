package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.repository.UserActivityLogRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserActivityLogService {
    UserActivityLogRepository userActivityLogRepository;
    public void log(UserActivityLog userActivityLog) {
        userActivityLogRepository.save(userActivityLog);
    }
    public List<UserActivityLog> getUserActivityLogs() {
        return userActivityLogRepository.findAll();
    }
}
