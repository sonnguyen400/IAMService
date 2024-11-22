package com.sonnguyen.iam.service;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.model.UserProfile;
import com.sonnguyen.iam.repository.UserProfileRepository;
import com.sonnguyen.iam.utils.ResponseMessage;
import com.sonnguyen.iam.utils.ResponseMessageStatus;
import com.sonnguyen.iam.viewmodel.UserProfileGetVm;
import com.sonnguyen.iam.viewmodel.UserProfilePostVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserAccountService userAccountService;
    AccountService accountService;
    UserActivityLogService userActivityLogService;
    CloudinaryService cloudinaryService;
    AbstractEmailService emailService;

    public UserProfile findById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public UserProfileGetVm findByAccountEmail(String accountEmail) {
        Account account = accountService.findByEmail(accountEmail).orElseThrow(() -> new ResourceNotFoundException("Account with email " + accountEmail + " not found"));
        UserProfile userProfile = userProfileRepository.findByAccount_id(account.getId()).orElseThrow(() -> new ResourceNotFoundException("User's profile with email " + accountEmail + " has yet set up"));
        return UserProfileGetVm.map(userProfile, account.getEmail());
    }

    public ResponseMessage setProfilePicture(String email, MultipartFile file) {
        UserProfile olduUserProfile = initUserProfile(email);
        String picture_url = (String) cloudinaryService.upload(file).get("url");
        olduUserProfile.setPicture_url(picture_url);
        userProfileRepository.save(olduUserProfile);
        emailService.sendEmail(email, "Update profile", "Your profile picture has been updated");
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PROFILE).build());
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message( "Update profile successfully")
                .build();
    }

    public ResponseMessage saveUserProfile(UserProfilePostVm userProfilePostVm) {
        UserProfile olduUserProfile = initUserProfile(userProfilePostVm.email());
        UserProfile newUserProfile = userProfilePostVm.toEntity();
        newUserProfile.setAccount_id(olduUserProfile.getAccount_id());
        newUserProfile.setId(olduUserProfile.getId());
        log.info("Saving user detail {}", olduUserProfile.getId());
        userProfileRepository.save(newUserProfile);
        emailService.sendEmail(userProfilePostVm.email(), "Update profile", "Your profile has been updated");
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PROFILE).build());
        return ResponseMessage.builder()
                .status(ResponseMessageStatus.SUCCESS.status)
                .message( "Update profile successfully")
                .build();
    }

    public UserProfile initUserProfile(String email) {
        Account account = accountService.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Account with email " + email + " not found"));
        Optional<UserProfile> userProfile = userProfileRepository.findByAccount_id(account.getId());
        return userProfile.orElseGet(() -> userProfileRepository.save(UserProfile
                .builder()
                .account_id(account.getId())
                .build()));
    }
}
