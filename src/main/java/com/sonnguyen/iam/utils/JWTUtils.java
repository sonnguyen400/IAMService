package com.sonnguyen.iam.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {
    private String secretKey = "fsigiwfysgiwyfgaiygiaygfdiyagddadad"; // Tạo token


    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))// 10 giờ
                .signWith(getSignKey()).compact();
    }
    public String generateToken(String subject, Long expirationTimeMillis) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date(expirationTimeMillis))
                .signWith(getSignKey()).compact();
    }
    public String generateToken(Map<String,Object> map,String subject, Long expirationTimeMillis) {
        return Jwts.builder().setSubject(subject).setClaims(map).setIssuedAt(new Date()).setExpiration(new Date(expirationTimeMillis))
                .signWith(getSignKey()).compact();
    }
    // Giải mã token và lấy thông tin người dùng
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
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