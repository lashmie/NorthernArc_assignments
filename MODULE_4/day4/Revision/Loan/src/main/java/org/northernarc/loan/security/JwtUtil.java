package org.northernarc.loan.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

	@Value("${app.jwt.secret:LoanAppSecretKeyForJwtAuthentication1234567890}")
	private String jwtSecret;

	@Value("${app.jwt.expiration-ms:86400000}")
	private long jwtExpirationMs;

	public String generateToken(String email, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);
		return createToken(claims, email);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtExpirationMs);
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(getSignInKey())
				.compact();
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
