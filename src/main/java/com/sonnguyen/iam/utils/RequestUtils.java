package com.sonnguyen.iam.utils;

import com.sonnguyen.iam.exception.ExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class RequestUtils {
    private JWTUtils jwtProvider;

    public Claims extractJwtClaimFromCookie(HttpServletRequest request, String cookieName)  {
        Cookie cookie= WebUtils.getCookie(request,cookieName);
        if(cookie != null){
            if(jwtProvider.isTokenExpired(cookie.getValue())){
                throw new ExpiredTokenException("Expired token");
            }
            return jwtProvider.extractClaims(cookie.getValue());
        }
        return null;
    }
    public Cookie generateExpired(String cookie){
        Cookie expiredCookie=new Cookie(cookie,"");
        expiredCookie.setMaxAge(0);
        return expiredCookie;
    }
    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName){
        return Optional.ofNullable(WebUtils.getCookie(request, cookieName));
    }
}
