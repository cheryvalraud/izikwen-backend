package com.example.Izikwen.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access}")
    private long accessExp;

    @Value("${jwt.expiration.2fa}")
    private long twoFaExp;

    @Value("${jwt.expiration.refresh}")
    private long refreshExp;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, long tokenVersion) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExp))
                .claims(Map.of(
                        "type", "access",
                        "ver", tokenVersion
                ))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email, long tokenVersion) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExp))
                .claims(Map.of(
                        "type", "refresh",
                        "ver", tokenVersion
                ))
                .signWith(getKey())
                .compact();
    }

    public String generateTwoFaToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + twoFaExp))
                .claims(Map.of("type", "2fa"))
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractType(String token) {
        Object type = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type");
        return type == null ? null : type.toString();
    }

    public long extractTokenVersion(String token) {
        Object ver = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("ver");

        if (ver == null) return 0;
        if (ver instanceof Number n) return n.longValue();
        return Long.parseLong(ver.toString());
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractType(token));
    }

    public boolean isAccessToken(String token) {
        return "access".equals(extractType(token));
    }
}
