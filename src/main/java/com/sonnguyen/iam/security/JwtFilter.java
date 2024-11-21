package com.sonnguyen.iam.security;

import com.sonnguyen.iam.model.UserDetailsImpl;
import com.sonnguyen.iam.service.AuthenticationService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class JwtFilter extends OncePerRequestFilter {
    RequestUtils requestUtils;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,@NotNull FilterChain filterChain) throws ServletException, IOException {
        Claims claims= requestUtils.extractJwtClaimFromCookie(request, AuthenticationService.authCookie);

        if(claims!=null&&claims.getSubject()!=null&& !claims.getSubject().isEmpty()){
            List<? extends GrantedAuthority> authorities = mapAuthorities((List<String>)claims.get("authorities", Collection.class));
            UserDetailsImpl userDetails=mapPrincipal(claims.get("principal",Map.class),authorities);
            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
    private static List<? extends  GrantedAuthority> mapAuthorities(Collection<String> list){
        return list.stream().map(SimpleGrantedAuthority::new).toList();
    }
    private static UserDetailsImpl mapPrincipal(Map<String,Object> map,  List<? extends GrantedAuthority>  authorities){
        return new UserDetailsImpl(((Integer)map.get("id")).longValue(),(String)map.get("email"),(Integer) map.get("consecutiveLoginFailures"), (boolean)map.get("isEnabled"),authorities);
    }
}
