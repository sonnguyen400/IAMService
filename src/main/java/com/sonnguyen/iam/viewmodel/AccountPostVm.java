package com.sonnguyen.iam.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record AccountPostVm(@NotNull @Email String email,@NotNull @Size(min = 8,message = "Password must be at least 8 characters at length") String password) {

}
