package com.sonnguyen.iam.viewmodel;

import jakarta.validation.constraints.NotNull;

public record LoginAcceptRequestVm(@NotNull String token, @NotNull String otp) {
}
