package com.internship.spotify.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
	private String jwtSecret;

	@Value("${jwt.expiration:86400000}") // 24 saat milisn
	private Long jwtExpiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public String generateToken(String username) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiration);

		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate).signWith(getSigningKey())
				.compact();
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isTokenExpired(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
			return claims.getExpiration().before(new Date());
		} catch (JwtException e) {
			return true;
		}
	}
}