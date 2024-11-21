package com.sonnguyen.iam.viewmodel;

import com.sonnguyen.iam.model.UserProfile;
import lombok.Builder;

import java.util.Date;
@Builder
public record UserProfileGetVm (String email,
                                String firstname,
                                String lastname,
                                Date dateOfBirth,
                                String gender,
                                String address,
                                String phoneNumber,
                                String picture_url
                                ){
    public static UserProfileGetVm map(UserProfile userProfile,String accountEmail){
        return UserProfileGetVm.builder()
                .email(accountEmail)
                .firstname(userProfile.getFirstname())
                .lastname(userProfile.getLastname())
                .dateOfBirth(userProfile.getDateOfBirth())
                .gender(userProfile.getGender())
                .address(userProfile.getAddress())
                .phoneNumber(userProfile.getPhoneNumber())
                .picture_url(userProfile.getPicture_url())
                .build();
    }
}
