package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/v1/auth/login") || request.getRequestURI().startsWith("/v1/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            email = jwtUtil.extractEmail(jwt);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(jwt, email)) {
                String systemRole = jwtUtil.extractSystemRole(jwt);
                Map<String, String> companyRoles = jwtUtil.extractCompanyRoles(jwt);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if (systemRole != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + systemRole));
                }
                if (companyRoles != null) {
                    companyRoles.values().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                auth.setDetails(Map.of("companyRoles", companyRoles));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
