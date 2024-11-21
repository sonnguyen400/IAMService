package com.sonnguyen.iam.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl {
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

    public boolean isEnabled() {
        return isEnabled;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonLocked() {
        return consecutiveLoginFailures<3;
    }
}
