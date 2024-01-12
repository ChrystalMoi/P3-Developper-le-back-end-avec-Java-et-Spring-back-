package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.services.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // Injection de dépendance du service RegisterService dans le contrôleur
    @Autowired
    private RegisterService registerService;

    // Endpoint pour gérer les requêtes de connexion
    @PostMapping("/login")
    public void login() {
        System.out.println("login ok");
    }

    // Endpoint pour gérer les requêtes d'inscription
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registrationRequest) {
        // Affiche un message de confirmation dans la console
        System.out.println("dans le register");
        try {
            // Appelle le service RegisterService pour effectuer l'inscription et récupère le token JWT résultant
            String token = registerService.registerUser(registrationRequest);

            System.out.println("register token created");

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(token);

        } catch (UserAlreadyExistsException e) {
            System.out.println("register exception UserAlreadyExistsException");

            // Retourne une exception car user existe déjà
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    /**
     * Register doit dans register()
     * (pas de code métier dans le controller donc il faut appeler un service
     * register où il y a le code métier):
     *
     * - créer un user en base
     * - générer un token jwt
     * - retourner en retour de la requete le token jwt
     *
     * La réponse doit être la même que la réponse de Mockoon
     */

}
