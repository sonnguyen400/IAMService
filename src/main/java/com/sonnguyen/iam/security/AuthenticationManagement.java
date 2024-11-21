package com.sonnguyen.iam.security;

import com.sonnguyen.iam.exception.AuthenticationException;
import com.sonnguyen.iam.model.UserDetailsImpl;
import com.sonnguyen.iam.service.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationManagement   {
    UserDetailsServiceImpl userDetailsService;
    Argon2PasswordEncoder passwordEncoder;
    public Authentication authenticate(String username, String password){
        UserDetailsImpl userDetails=userDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(password,userDetails.getPassword())){
            if(!userDetails.isEnabled()) throw new AuthenticationException("Account is disabled");
            if(!userDetails.isAccountNonLocked()) throw new AuthenticationException("Account is locked");
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        else throw new AuthenticationException("Invalid password");
    }

}
