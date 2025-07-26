package com.hirepath.hirepath_backend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private String SECRET_KEY = "hirepath-secret-jamy-hirepath-secret-jamy-hirepath-secret-jamy";
    private long EXPIRATION_TIME = 60 * 60 * 1000;
    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes((StandardCharsets.UTF_8)));

    public String generateToken(String email, String systemRole, Map<String, String> companyRoles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("systemRole", systemRole);
        if (companyRoles != null && !companyRoles.isEmpty()) {
            claims.put("companyRoles", companyRoles);
        }
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage());
        }
    }

    public String extractSystemRole(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("systemRole", String.class);
    }

    public Map extractCompanyRoles(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("companyRoles", Map.class);
        }
        catch (Exception e) {
            return new HashMap<>();
        }
    }

    public boolean isTokenValid(String token, String email) {
        try {
            String tokenEmail = extractEmail(token);
            return (tokenEmail.equals(email) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
