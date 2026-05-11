package com.gestor.e_commerce.service;

import com.gestor.e_commerce.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private static final String ADMIN_USER = "admin";
    private static final String USER_USER = "user";
    private static final String PASSWORD = "1234";

    public Map<String, String> login(String user, String pass) {

        String role = authenticate(user, pass);

        if (role == null) {
            return Map.of("error", "Invalid credentials");
        }

        return Map.of("token", generateToken(user, role));
    }

    public String getAuthenticatedUser(String token) {

        String cleanToken = extractToken(token);
        return JwtUtil.getUsername(cleanToken);
    }

    // ----------------- PRIVATE METHODS -----------------

    private String authenticate(String user, String pass) {

        if (isValid(user, pass, ADMIN_USER)) {
            return "ADMIN";
        }

        if (isValid(user, pass, USER_USER)) {
            return "USER";
        }

        return null;
    }

    private boolean isValid(String user, String pass, String expectedUser) {
        return expectedUser.equals(user) && PASSWORD.equals(pass);
    }

    private String generateToken(String user, String role) {
        return JwtUtil.generateToken(user, role);
    }

    private String extractToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format");
        }
        return token.substring(7);
    }
}