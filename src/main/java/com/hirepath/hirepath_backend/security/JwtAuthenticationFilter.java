package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/v1/auth/login") || request.getRequestURI().equals("/v1/user/register")) {
            chain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        try {
            if (header != null && header.startsWith("Bearer ")) {
                jwt = header.substring(7);
                email = jwtUtil.extractEmail(jwt);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.isTokenValid(jwt, email)) {
                    String tokenType = jwtUtil.extractTokenType(jwt);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    UsernamePasswordAuthenticationToken auth = null;

                    if ("SYSTEM".equals(tokenType)) {
                        String systemRole = jwtUtil.extractSystemRole(jwt);
                        if (systemRole != null) {
                            authorities.add(new SimpleGrantedAuthority(systemRole));
                            if ("ADMIN".equals(systemRole)) {
                                authorities.add(new SimpleGrantedAuthority("USER"));
                            }
                        }
                        auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    } else if ("COMPANY".equals(tokenType)) {
                        String companyRole = jwtUtil.extractCompanyRole(jwt);
                        String companyGuid = jwtUtil.extractCompanyGuid(jwt);
                        if (companyRole != null) {
                            authorities.add(new SimpleGrantedAuthority(companyRole));
                        }
                        auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                        if (companyGuid != null) {
                            auth.setDetails(Map.of("companyGuid", companyGuid));
                        }
                    }

                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
            chain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JWT validation failed for request: {} - {}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"data\": null, \"message\": \"Invalid or expired token\"}");
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter for request: {} - {}", request.getRequestURI(), e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"data\": null, \"message\": \"An unexpected error occurred\"}");
        }
    }
}
