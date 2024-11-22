package com.sonnguyen.iam.security;

import com.sonnguyen.iam.exception.AuthenticationException;
import com.sonnguyen.iam.service.AuthenticationService;
import com.sonnguyen.iam.service.ForbiddenTokenService;
import com.sonnguyen.iam.utils.JWTUtils;
import com.sonnguyen.iam.utils.RequestUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    JWTUtils jwtUtils;
    ForbiddenTokenService forbiddenTokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Claims claims = validateToken(request);
        if (claims != null && !claims.getSubject().isEmpty()) {
            Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims.get("scope", String.class));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private Claims validateToken(HttpServletRequest request) {
        try {
            String token = RequestUtils.extractValueFromCookie(request, AuthenticationService.authCookie);
            if (forbiddenTokenService.existsByToken(token)) {
                throw new AuthenticationException("Forbidden token");
            }
            ;
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        List<String> scopes = Arrays.stream(token.split(" ")).toList();
        return scopes.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
