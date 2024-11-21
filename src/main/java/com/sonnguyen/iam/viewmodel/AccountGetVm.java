package com.sonnguyen.iam.viewmodel;

import com.sonnguyen.iam.model.Account;

public record AccountGetVm(Long id,String email) {
    public static AccountGetVm fromAccount(Account account) {
        return new AccountGetVm(account.getId(), account.getEmail());
    }
}
