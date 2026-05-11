package com.gestor.e_commerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key SECRET =
            Keys.hmacShaKeyFor("my-secret-key-my-secret-key-123456".getBytes());

    // GENERATE TOKEN
    public static String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET)
                .compact();
    }

    // GET ALL CLAIMS (avoid duplication)
    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // READ USERNAME
    public static String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // READ ROLE
    public static String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}