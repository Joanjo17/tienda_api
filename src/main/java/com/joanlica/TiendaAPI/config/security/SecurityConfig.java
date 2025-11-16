package com.joanlica.TiendaAPI.config.security;

import com.joanlica.TiendaAPI.config.security.filters.JwtTokenValidator;
import com.joanlica.TiendaAPI.config.util.JwtUtils;
import com.joanlica.TiendaAPI.core.handler.ErrorResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final ErrorResponseFactory errorFactory;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationEntryPoint jsonAuthenticationEntryPoint,
                                           AccessDeniedHandler jsonAccessDeniedHandler,
                                           JwtTokenValidator jwtTokenValidator) throws Exception {

        return http
                // 1. Aplica la configuración CORS primero
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)   // ← No usamos esto
                .formLogin(AbstractHttpConfigurer::disable)   // ← No usamos esto
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Para Auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/roles/**").hasAnyRole("ADMIN")
                        // Para Clientes
                        .requestMatchers("/api/v1/clientes/me").authenticated()
                        .requestMatchers("/api/v1/clientes/**").hasAnyRole("ADMIN")

                        // Para Producto
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/{codigo_producto}").permitAll()
                        .requestMatchers("/api/v1/productos/**").hasAnyRole("ADMIN")

                        // Para Ventas
                        .requestMatchers(HttpMethod.POST, "/api/v1/ventas/").authenticated()
                        .requestMatchers("/api/v1/ventas/**").hasAnyRole("ADMIN")

                        .anyRequest().denyAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(jsonAuthenticationEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //Seteamos el UserDetailsService y el Password Encoder.
        DaoAuthenticationProvider p = new DaoAuthenticationProvider(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenValidator jwtTokenValidator(AuthenticationEntryPoint entryPoint) {
        return new JwtTokenValidator(jwtUtils, entryPoint);
    }

    @Bean
    public AuthenticationEntryPoint jsonAuthenticationEntryPoint() {
        return (req, res, ex) -> {
            res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            errorFactory.write(res, 401, "unauthorized", "Authentication required",
                    req.getRequestURI(), Map.of());
        };
    }

    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return (req, res, ex) ->
                errorFactory.write(res, 403, "forbidden", "Access denied",
                        req.getRequestURI(), Map.of());
    }
}