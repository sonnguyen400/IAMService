package com.sonnguyen.iam.viewmodel;

import com.sonnguyen.iam.model.UserProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UserProfilePostVm(
        @NotNull @Email String email,
        @NotNull @NotBlank String firstname,
        @NotNull @NotBlank String lastname,
        @NotNull Date dateOfBirth,
        @NotNull @NotBlank String gender,
        @NotNull @NotBlank String address,
        @NotNull @NotBlank String phoneNumber
) {
    public UserProfile toEntity() {
        return UserProfile.builder()
                .firstname(firstname)
                .lastname(lastname)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
