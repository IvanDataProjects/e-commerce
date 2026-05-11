package com.gestor.e_commerce.service;


import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String getUserFromToken(String token) {

        String cleanToken = token.replace("Bearer ", "");
        return jwtService.extractUsername(cleanToken);
    }
}
