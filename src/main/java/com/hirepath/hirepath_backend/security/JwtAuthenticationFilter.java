package com.hirepath.hirepath_backend.security;

import com.hirepath.hirepath_backend.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

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
                            authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM"));
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + systemRole));
                            if (Objects.equals("ADMIN", systemRole)) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                            }
                        }
                        auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    }
                    else if ("COMPANY".equals(tokenType)) {
                        String companyRole = jwtUtil.extractCompanyRole(jwt);
                        String companyGuid = jwtUtil.extractCompanyGuid(jwt);
                        if (companyRole != null) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + companyRole));
                            if (List.of("COMPANY_OWNER", "COMPANY_ADMIN", "COMPANY_EMPLOYEE").contains(companyRole)) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_COMPANY"));
                            }
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
        } catch (ResponseStatusException e) {
            log.error("JWT validation failed for request: {} - {}", request.getRequestURI(), e.getMessage());
            response.setStatus(e.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"success\": false, \"data\": null, \"message\": \"%s\"}", e.getReason()));
        }
    }
}
