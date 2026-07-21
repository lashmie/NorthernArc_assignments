package org.northernarc.minion.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.northernarc.minion.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret:minion-secret-key-minion-secret-key-123456}") String secret,
                      @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(Customer customer) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(customer.getEmail())
                .claim("role", customer.getRole().name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public long extractExpiry(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    public boolean isExpired(String token) {
        return extractExpiry(token) < System.currentTimeMillis();
    }
}

