package com.sonnguyen.iam.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    private String secretKey = "your_secret_key"; // Tạo token

    public String generateToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))// 10 giờ
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    public String generateToken(String subject, Long expirationTimeMillis) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date(expirationTimeMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    // Giải mã token và lấy thông tin người dùng
    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    // Lấy subject từ token
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    // Kiểm tra xem token có hết hạn không
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Xác thực token
    public boolean validateToken(String token, String subject) {
        return (subject.equals(extractSubject(token)) && !isTokenExpired(token));
    }
}