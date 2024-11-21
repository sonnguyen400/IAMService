package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.Account;
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
    AccountService accountService;
    CloudinaryService cloudinaryService;
    public UserProfile findById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));
    }
    public UserProfileGetVm findByAccountEmail(String accountEmail) {
        Account account= accountService.findByEmail(accountEmail).orElseThrow(()->new ResourceNotFoundException("Account with email " + accountEmail + " not found"));
        UserProfile userProfile = userProfileRepository.findByAccount_id(account.getId()).orElseThrow(()->new ResourceNotFoundException("User with id " + account.getId() + " not found"));
        return UserProfileGetVm.map(userProfile,account.getEmail());
    }
    public String saveUserProfile(UserProfilePostVm userProfilePostVm, MultipartFile file) {
        Account account=accountService.findByEmail(userProfilePostVm.email()).orElseThrow(()->new ResourceNotFoundException("Account with email " + userProfilePostVm.email() + " not found"));
        UserProfile userProfile=userProfilePostVm.toEntity();
        userProfile.setAccount_id(account.getId());
        Optional<UserProfile> existedProfile=userProfileRepository.findByAccount_id(account.getId());
        existedProfile.ifPresent(profile -> userProfile.setId(profile.getId()));
        log.info("Saving user detail {}", userProfile.getId());
        if(file!=null){
            String picture_url=(String) cloudinaryService.upload(file).get("url");
            userProfile.setPicture_url(picture_url);
        }
        userProfileRepository.save(userProfile);
        return "Update profile successfully";
    }
}
