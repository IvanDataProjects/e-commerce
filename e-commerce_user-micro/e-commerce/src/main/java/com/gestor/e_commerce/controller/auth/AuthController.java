package com.gestor.e_commerce.controller.auth;

import com.gestor.e_commerce.security.JwtUtil;
import com.gestor.e_commerce.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/e-commerce/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String user,
                                     @RequestParam String pass) {

        return authService.login(user, pass);
    }
}