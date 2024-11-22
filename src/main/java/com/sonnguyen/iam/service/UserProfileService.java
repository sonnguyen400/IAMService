package com.sonnguyen.iam.service;

import com.sonnguyen.iam.constant.ActivityType;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.model.UserActivityLog;
import com.sonnguyen.iam.model.UserProfile;
import com.sonnguyen.iam.repository.UserProfileRepository;
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
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserAccountService userAccountService;
    AccountService accountService;
    UserActivityLogService userActivityLogService;
    CloudinaryService cloudinaryService;
    AbstractEmailService emailService;
    public UserProfile findById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));
    }
    public UserProfileGetVm findByAccountEmail(String accountEmail) {
        Account account= accountService.findByEmail(accountEmail).orElseThrow(()->new ResourceNotFoundException("Account with email " + accountEmail + " not found"));
        UserProfile userProfile = userProfileRepository.findByAccount_id(account.getId()).orElseThrow(()->new ResourceNotFoundException("User's profile with email " + accountEmail + " has yet set up"));
        return UserProfileGetVm.map(userProfile,account.getEmail());
    }
    public String setProfilePicture(String email,MultipartFile file) {
        UserProfile userProfile=initUserProfile(email);
        String picture_url=(String) cloudinaryService.upload(file).get("url");
        userProfile.setPicture_url(picture_url);
        userProfileRepository.save(userProfile);
        emailService.sendEmail(email,"Update profile","Your profile picture has been updated");
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PROFILE).build());
        return "Set profile picture successfully";
    }
    public String saveUserProfile(UserProfilePostVm userProfilePostVm) {
        UserProfile userProfile=initUserProfile(userProfilePostVm.email());
        UserProfile newUserProfile=userProfilePostVm.toEntity();
        newUserProfile.setAccount_id(userProfile.getAccount_id());
        log.info("Saving user detail {}", userProfile.getId());
        userProfileRepository.save(userProfile);
        emailService.sendEmail(userProfilePostVm.email(),"Update profile","Your profile has been updated");
        userActivityLogService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PROFILE).build());
        return "Update profile successfully";
    }
    public UserProfile initUserProfile(String email) {
        Account account=accountService.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Account with email " +email+ " not found"));
        Optional<UserProfile> userProfile= userProfileRepository.findByAccount_id(account.getId());
        return userProfile.orElseGet(() -> userProfileRepository.save(UserProfile
                .builder()
                .account_id(account.getId())
                .build()));
    }
}
