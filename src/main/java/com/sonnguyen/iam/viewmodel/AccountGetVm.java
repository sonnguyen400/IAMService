package com.sonnguyen.iam.viewmodel;

import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.model.UserProfile;

public record AccountGetVm(Long id,String email, UserProfile userProfile) {
    public static AccountGetVm fromAccount(Account account) {
        return new AccountGetVm(account.getId(), account.getEmail(),account.getUserProfile() );
    }
}
