package com.gestor.e_commerce.controller.auth;

import com.gestor.e_commerce.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/e-commerce/auth")
public class SecureController {

    private final AuthService authService;

    public SecureController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public String test(@RequestHeader("Authorization") String token) {
        String user = authService.getUserFromToken(token);
        return "Access granted to: " + user;
    }
}
