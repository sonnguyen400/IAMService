package com.sonnguyen.iam.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {
    private final RSAKeyUtil rsaKeyUtil;

    public JWTUtils(RSAKeyUtil rsaKeyUtil) {
        this.rsaKeyUtil = rsaKeyUtil;
    }

    public String generateToken(String username) throws Exception {
        return generateToken(username, Instant.now().plus(3, ChronoUnit.HOURS));
    }

    public String generateToken(String subject, Instant expiration) throws Exception {
        return generateToken(new HashMap<>(), subject, expiration);
    }

    public String generateToken(Map<String, Object> claims, String subject, Instant expiration) throws Exception {
        PrivateKey privateKey = rsaKeyUtil.getPrivateKey();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date()).setExpiration(Date.from(expiration)) // 1 giờ
                .signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    public Claims validateToken(String token) throws Exception {
        PublicKey publicKey = rsaKeyUtil.getPublicKey();
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token)
                .getBody();
    }
}