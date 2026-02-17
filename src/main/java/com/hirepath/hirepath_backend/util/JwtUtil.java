package com.hirepath.hirepath_backend.util;

import com.hirepath.hirepath_backend.model.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "hirepath-secret-jamy-hirepath-secret-jamy-hirepath-secret-jamy";
    private final long EXPIRATION_TIME = 60 * 60 * 10000000;
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateSystemToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "SYSTEM");
        claims.put("systemRole", user.getRole().getName());
        return generateToken(user.getEmail(), claims);
    }

    public String generateCompanyToken(User user, String companyGuid, String companyRole) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "COMPANY");
        claims.put("companyGuid", companyGuid);
        claims.put("companyRole", companyRole);
        return generateToken(user.getEmail(), claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        try {
            return getAllClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token: " + e.getMessage());
        }
    }

    public String extractTokenType(String token) {
        try {
            return getAllClaimsFromToken(token).get("tokenType", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String extractSystemRole(String token) {
        try {
            return getAllClaimsFromToken(token).get("systemRole", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String extractCompanyRole(String token) {
        try {
            return getAllClaimsFromToken(token).get("companyRole", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String extractCompanyGuid(String token) {
        try {
            return getAllClaimsFromToken(token).get("companyGuid", String.class);
        } catch (Exception e) {
            return null;
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
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }
}
