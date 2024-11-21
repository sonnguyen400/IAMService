package com.sonnguyen.iam.utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
            return jwtProvider.extractClaims(cookie.getValue());
        }
        return null;
    }
    public Cookie getExpiredCookie(String cookie){
        Cookie expiredCookie=new Cookie(cookie,"");
        expiredCookie.setMaxAge(0);
        return expiredCookie;
    }
    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName){
        return Optional.ofNullable(WebUtils.getCookie(request, cookieName));
    }
}
