package com.openclassrooms.projet3.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Composant Spring gérant la récupération de la clé secrète JWT
 * depuis les propriétés de l'app
 */
@Component
public class MySecretKey {

    // La clé secrète JWT récupérée depuis les propriétés de l'app
    @Value("${jwt.secretKey}")
    private String secretKey;

    /**
     * Renvoie la clé secrète JWT
     * @return La clé secrète JWT configurée dans les propriétés de l'app
     */
    public String getSecretKey() {
        return secretKey;
    }
}
