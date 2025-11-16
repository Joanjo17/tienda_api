package com.joanlica.TiendaAPI.config.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joanlica.TiendaAPI.config.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Si ya hay autenticación previa en el contexto, seguimos sin revalidar
        var existing = SecurityContextHolder.getContext().getAuthentication();
        if (existing == null || existing instanceof AnonymousAuthenticationToken) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header != null) {
                String trimmed = header.trim();
                // soporta "Bearer " case-insensitive
                if (trimmed.length() >= 7 && trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
                    String jwtToken = trimmed.substring(7).trim(); // "Bearer " + token
                    if (jwtToken.isEmpty()) {
                        SecurityContextHolder.clearContext();
                        authEntryPoint.commence(request, response,
                                new InsufficientAuthenticationException("Missing bearer token"));
                        return;
                    }
                    try {
                        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                        String username = jwtUtils.extractUsername(decodedJWT);
                        List<String> authoritiesClaim = jwtUtils
                                .getSpecificClaim(decodedJWT, "authorities")
                                .asList(String.class);

                        Collection<? extends GrantedAuthority> authoritiesList =
                                (authoritiesClaim == null ? List.<String>of() : authoritiesClaim).stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList();

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        //Lo seteamos en el contexto de seguridad.
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authentication);
                        SecurityContextHolder.setContext(context);

                    } catch (JWTVerificationException ex) {
                        SecurityContextHolder.clearContext();
                        authEntryPoint.commence(
                                request, response,
                                new InsufficientAuthenticationException("Invalid token", ex)
                        );
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);

    }
}