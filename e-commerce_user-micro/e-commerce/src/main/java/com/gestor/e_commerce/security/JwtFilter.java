package com.gestor.e_commerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        // 1. INTERCEPT REQUEST (every request passes here)
        String authHeader = request.getHeader("Authorization");

        // 2. IF NO TOKEN → ALLOW REQUEST (public endpoint or no auth required)
        if (!hasBearerToken(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            // 3. VALIDATE TOKEN (extract JWT information)
            String token = extractToken(authHeader);

            String username = JwtUtil.getUsername(token);
            String role = JwtUtil.getRole(token);

            // 4. CREATE AUTHENTICATED USER (Spring Security Context)
            setAuthentication(username, role);

        } catch (Exception e) {

            // INVALID TOKEN → BLOCK REQUEST
            sendUnauthorized(response);
            return;
        }

        // 5. CONTINUE FILTER CHAIN (goes to controller if everything is OK)
        filterChain.doFilter(request, response);
    }

    // ---------------- PRIVATE METHODS ----------------

    private boolean hasBearerToken(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    private String extractToken(String header) {
        return header.substring(7);
    }

    private void setAuthentication(String username, String role) {

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void sendUnauthorized(HttpServletResponse response) throws IOException {

        // TOKEN INVALID → RETURN 401 RESPONSE
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
    }
}