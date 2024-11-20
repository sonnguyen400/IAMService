package com.sonnguyen.iam.service;

import com.sonnguyen.iam.exception.InvalidArgumentException;
import com.sonnguyen.iam.exception.ResourceNotFoundException;
import com.sonnguyen.iam.model.UserProfile;
import com.sonnguyen.iam.repository.UserProfileRepository;
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
    CloudinaryService cloudinaryService;
    public UserProfile findById(Long id) {
        return userProfileRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));
    }
    public UserProfile saveUserProfile(UserProfile userProfile, MultipartFile file) {
        Long accountid=0L;
        if(userProfile.getAccount()!=null&&userProfile.getAccount().getId()!=null){
            accountid = userProfile.getAccount().getId();
        }else throw new InvalidArgumentException("Account id is required");

        Optional<UserProfile> existedProfile=userProfileRepository.findByAccount_id(accountid);
        existedProfile.ifPresent(profile -> userProfile.setId(profile.getId()));

        if(file!=null){
            String picture_url=(String) cloudinaryService.upload(file).get("url");
            userProfile.setPicture_url(picture_url);
        }
        return userProfileRepository.save(userProfile);
    }
}
