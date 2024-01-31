package com.openclassrooms.projet3.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")  // autorise toutes les origines
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // autorise les méthodes HTTP
                        .allowedHeaders("*");  // autorise tous les en-têtes
            }
        };
    }
}