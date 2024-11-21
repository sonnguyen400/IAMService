package com.sonnguyen.iam.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangingPasswordPostVm(@NotNull @Size(min = 8,message = "Password must be at least 8 characters at length") String oldPassword,
                                     @NotNull @Size(min = 8,message = "Password must be at least 8 characters at length") String newPassword,
                                     @NotNull @Email String email)
{
}
