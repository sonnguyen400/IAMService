package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.PermissionDetail;
import com.sonnguyen.iam.model.UserDetailsImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserDetailsServiceImpl {
    AccountService accountService;
    PermissionDetailService permissionDetailService;
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountService
                .findByEmail(email)
                .map((account -> {
                    UserDetailsImpl userDetails = new UserDetailsImpl(account);
                    List<PermissionDetail> permissionDetails=permissionDetailService.findAllByAccountId(account.getId());
                    userDetails.mapAuthority(permissionDetails);
                    return userDetails;
                }))
                .orElseThrow(()->new UsernameNotFoundException(String.format("Can't not find user with email %s",email)));
    }
}
