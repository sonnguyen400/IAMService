package com.sonnguyen.iam.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Getter
@Setter
public class UserDetailsImpl {
    private Long id;
    private String email;
    private String password;
    private Integer consecutiveLoginFailures;
    private Boolean isEnabled;
    private List<? extends GrantedAuthority> authorities;
    public UserDetailsImpl(Account account) {
        id=account.getId();
        email=account.getEmail();
        password=account.getPassword();
        consecutiveLoginFailures=account.getConsecutiveLoginFailures();
        isEnabled=account.getIsEnabled();
    }

    public UserDetailsImpl(Long id, String email,Integer consecutiveLoginFailures, Boolean isEnabled, List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.consecutiveLoginFailures = consecutiveLoginFailures;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public void mapAuthority(List<PermissionDetail> authorities) {
        this.authorities=authorities.stream().map((authority_)->new SimpleGrantedAuthority(authority_.getName())).toList();
    }
    public boolean isEnabled() {
        return isEnabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
