package org.northernarc.loanminiproject.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtil {

    private static final String SECRET_STRING = "your-super-secret-key-that-is-at-least-32-characters-long!";
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email) {
        long expiry = System.currentTimeMillis() + EXPIRATION_TIME;
        String payload = base64UrlEncode(email) + "." + expiry;
        String signature = sign(payload);
        return payload + "." + signature;
    }

    public String extractUsername(String token) {
        if (!isTokenStructureValid(token) || !hasValidSignature(token) || isTokenExpired(token)) {
            return null;
        }
        String[] parts = token.split("\\.");
        return new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
    }

    private Date extractExpiration(String token) {
        String[] parts = token.split("\\.");
        long expiry = Long.parseLong(parts[1]);
        return new Date(expiry);
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception ex) {
            return true;
        }
    }

    private boolean isTokenStructureValid(String token) {
        return token != null && token.split("\\.").length == 3;
    }

    private boolean hasValidSignature(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = parts[0] + "." + parts[1];
            String expected = sign(payload);
            return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            return false;
        }
    }

    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(SECRET_STRING.getBytes(StandardCharsets.UTF_8), HMAC_ALGO));
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Token signing failed", ex);
        }
    }

    private String base64UrlEncode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
}

