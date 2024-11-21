package com.sonnguyen.iam.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

public class RequestUtils {
    public static String extractValueFromCookie(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public Cookie getExpiredCookie(String cookie) {
        Cookie expiredCookie = new Cookie(cookie, "");
        expiredCookie.setMaxAge(0);
        return expiredCookie;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        return Optional.ofNullable(WebUtils.getCookie(request, cookieName));
    }
}
