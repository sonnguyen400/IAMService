package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.model.UserProfile;
import com.sonnguyen.iam.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserProfileController {
    UserProfileService userProfileService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CHANGE_USER_PROFILE') or (hasAnyAuthority('CHANGE_PROFILE') and #userProfile.account_id=authentication.principal.id)")
    public UserProfile createNewProfile(@RequestPart(name = "picture") MultipartFile picture,
                                        @Valid UserProfile userProfile) {
        return userProfileService.saveUserProfile(userProfile, picture);
    }
}
