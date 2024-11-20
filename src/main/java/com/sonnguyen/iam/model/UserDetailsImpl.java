package com.sonnguyen.iam.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer consecutiveLoginFailures;
    private Boolean isEnabled;
    public UserDetailsImpl(Account account) {
        id=account.getId();
        email=account.getEmail();
        password=account.getPassword();
        consecutiveLoginFailures=account.getConsecutiveLoginFailures();
        isEnabled=account.getIsEnabled();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return consecutiveLoginFailures<3;
    }
}
