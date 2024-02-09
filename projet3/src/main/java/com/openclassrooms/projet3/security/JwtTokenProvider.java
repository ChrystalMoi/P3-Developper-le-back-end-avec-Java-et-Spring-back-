package com.openclassrooms.projet3.security;

import com.openclassrooms.projet3.entites.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Gère la création, la validation et l'extraction des tokens JWT
 */
@Component
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;

    // Constructeur
    public JwtTokenProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // Clé secrète utilisée pour signer les tokens JWT
    @Value("${jwt.secretKey}")
    private String secretKey;

    // Durée de validité d'un token JWT (en millisecondes)
    @Value("${jwt.expirationInMs}")
    private long expirationInMs;

    // Génère un token JWT en fonction de l'adresse e-mail de l'utilisateur

    /**
     * Génère un jeton JWT (JSON Web Token) pour l'utilisateur donné. <br>
     * Ce jeton contient les revendications (claims) pertinentes pour l'utilisateur, telles que son identifiant, son nom, son adresse e-mail, et les horodatages de création et de mise à jour. <br>
     * Le jeton est signé avec un algorithme HMAC SHA-256.
     *
     * @param userEntity L'entité représentant l'utilisateur pour lequel générer le jeton JWT.
     * @return String - Le jeton JWT généré, encodé sous forme de chaîne de caractères.
     */
    public String generateToken(UserEntity userEntity) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userEntity.getEmail())
                .claim("id", userEntity.getId())
                .claim("name", userEntity.getName())
                .claim("email", userEntity.getEmail())
                .claim("created_at", userEntity.getCreatedAt())
                .claim("updated_at", userEntity.getUpdatedAt())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
