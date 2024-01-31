package com.openclassrooms.projet3;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtCoderConfig {

    // Clé secrete extraite d'application.properties
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.secretKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(), 0, this.secretKey.getBytes().length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
