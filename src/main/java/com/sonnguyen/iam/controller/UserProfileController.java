package com.sonnguyen.iam.controller;

import com.sonnguyen.iam.service.UserProfileService;
import com.sonnguyen.iam.viewmodel.UserProfileGetVm;
import com.sonnguyen.iam.viewmodel.UserProfilePostVm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CHANGE_USER_PROFILE') or (hasAnyAuthority('CHANGE_PROFILE') and #userProfile.email()==authentication.principal)")
    public String createNewProfile(@Valid UserProfilePostVm userProfile) {

        return userProfileService.saveUserProfile(userProfile);
    }

    @PostMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CHANGE_USER_PROFILE') or (hasAnyAuthority('CHANGE_PROFILE') and email()==authentication.principal)")
    public String setUploadProfilePicture(@NotNull @RequestPart(name = "picture") MultipartFile picture, @NotNull @NotBlank String email) {
        return userProfileService.setProfilePicture(email, picture);
    }

    @GetMapping(value = "/email")
    public UserProfileGetVm findProfileByEmail(@RequestParam(required = false) String email) {
        return userProfileService.findByAccountEmail(email);
    }
}
