package com.gestor.e_commerce.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key secretKey;

    public JwtService() {
        this.secretKey = Keys.hmacShaKeyFor(
                "my-secret-key-my-secret-key-123456".getBytes()
        );
    }

    // GENERATE TOKEN
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

    // PARSE CLAIMS (CENTRALIZED)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // EXTRACT USERNAME
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // EXTRACT ROLE
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
