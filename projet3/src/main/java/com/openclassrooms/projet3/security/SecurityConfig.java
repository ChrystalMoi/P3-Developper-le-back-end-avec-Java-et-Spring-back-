package com.openclassrooms.projet3.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configure et retourne un filtre de sécurité pour gérer les requêtes HTTP entrantes. <br> <br>
     * Ce filtre désactive la protection CSRF (Cross-Site Request Forgery), la connexion par formulaire, et configure la gestion des sessions comme Stateless. <br>
     * De plus, il ajoute un filtre JWT (JSON Web Token) avant le filtre d'authentification par nom d'utilisateur/mot de passe. <br>
     * Enfin, il définit les autorisations pour les différentes requêtes HTTP, en permettant l'accès aux endpoints d'inscription, de connexion, de récupération des informations utilisateur (profil), ainsi qu'aux endpoints Swagger. <br>
     * <br>
     * @param http HttpSecurity - Le constructeur de configuration de sécurité HTTP à utiliser pour configurer le filtre.
     * @return SecurityFilterChain - Le filtre de sécurité configuré.
     * @throws Exception Si une erreur se produit lors de la configuration de la sécurité HTTP.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/register", "/api/auth/login", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/api/auth/me").permitAll();
                    auth.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
