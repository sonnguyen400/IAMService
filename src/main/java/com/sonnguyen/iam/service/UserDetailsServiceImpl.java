package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.Account;
import com.sonnguyen.iam.model.UserDetailsImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountService
                .findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(()->new UsernameNotFoundException(String.format("Can't not find user with email %s",email)));
    }
}
