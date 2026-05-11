package com.gestor.e_commerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // DESACTIVA PROTECCIONES POR DEFECTO (CSRF)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // DEFINIR PERMISOS (QUIÉN PUEDE ACCEDER A QUÉ)
                .authorizeHttpRequests(auth -> auth

                        // 1. ROLES USER / ADMIN (LECTURA)
                        // USER puede entrar
                        // ADMIN también puede entrar
                        .requestMatchers(HttpMethod.GET, "/e-commerce/order/**")
                        .hasAnyRole("USER", "ADMIN")

                        // 2. SOLO ADMIN (ESCRITURA)
                        // SOLO ADMIN puede crear
                        .requestMatchers(HttpMethod.POST, "/e-commerce/order/**")
                        .hasRole("ADMIN")

                        // 3. SOLO ADMIN (BORRAR)
                        .requestMatchers(HttpMethod.DELETE, "/e-commerce/order/**")
                        .hasRole("ADMIN")

                        // 4. TODO LO DEMÁS REQUIERE LOGIN
                        .anyRequest().authenticated()
                )

                // REGISTRAR FILTRO JWT
                // aquí se mete el JwtFilter en la cadena de seguridad
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
