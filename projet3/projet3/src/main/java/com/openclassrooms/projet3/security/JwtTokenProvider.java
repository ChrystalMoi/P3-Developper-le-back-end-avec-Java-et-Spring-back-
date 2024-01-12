package com.openclassrooms.projet3.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Gère la création, la validation et l'extraction des tokens JWT
 */
@Component
public class JwtTokenProvider {
    // Clé secrète utilisée pour signer les tokens JWT
    @Value("${jwt.secretKey}")
    private String secretKey;

    // Durée de validité d'un token JWT (en millisecondes)
    @Value("${jwt.expirationInMs}")
    private long expirationInMs;

    // Génère un token JWT en fonction de l'adresse e-mail de l'utilisateur
    public String generateToken(String email) {
        try {
            // Encodage de l'e-mail en Base64URL avec UTF-8 explicite
            String encodedEmail = new String(Base64.encodeBase64URLSafe(email.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            // Affiche l'e-mail encodé dans les logs
            System.out.println("Encoded Email: " + encodedEmail);

            // Date actuelle
            Date now = new Date();

            // Date d'expiration du token
            Date expiryDate = new Date(now.getTime() + expirationInMs);

            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    //.signWith(SignatureAlgorithm.HS512, secretKey)
                    .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            throw new JwtException("Erreur lors de la génération du token JWT", e);
        }
    }

    /**
     * Génération d'une clé secrete de type Key
     * @return une clé secrète
     */
    public Key getSecretKey(){
        byte[] keyByte = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }

    // Extrait l'e-mail à partir d'un token JWT
    public String getEmailFromToken(String token) {
        try {
            // Décode la partie sujet du token (l'e-mail encodé)
            String encodedEmail = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            // Affiche l'e-mail encodé dans les logs
            System.out.println("Encoded Email from Token: " + encodedEmail);

            try {
                // Decode l'e-mail
                return new String(Base64.decodeBase64(encodedEmail), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new JwtException("Erreur lors du décodage de l'e-mail depuis le token JWT", ex);
            }

        } catch (Exception ex) {
            // Gestion des exceptions en cas d'erreur de décodage ou de validation du token
            throw new JwtException("Erreur lors de la récupération de l'e-mail depuis le token JWT", ex);
        }
    }

    // Valide un token JWT en vérifiant sa signature et sa date d'expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Affiche le message d'erreur en cas d'échec de validation
            System.err.println("Erreur de validation du token : " + ex.getMessage());
            return false;
        }
    }
}
