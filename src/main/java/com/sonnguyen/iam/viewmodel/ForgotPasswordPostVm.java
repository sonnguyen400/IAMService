package com.sonnguyen.iam.viewmodel;

import jakarta.validation.constraints.NotNull;

public record ForgotPasswordPostVm(@NotNull String token,@NotNull String newPassword) {
}
