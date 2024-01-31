package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.security.JwtTokenProvider;
import com.openclassrooms.projet3.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints related to Authentification")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class);

    // Injection de dépendance du service RegisterService et loginService dans le contrôleur
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    //Constructeur qui remplace l'autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Endpoint pour gérer les requêtes de connexion
    // @ApiOperation(value = "Login to the application", produces = "application/json")
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        LOGGER.info("login ok");

        try{
            // Appelle le service LoginService pour effectuer la connexion et récupère le token JWT résultant
            String token = authService.loginUser(userLoginRequest);

            LOGGER.info("login token created");

            // objet json sous forme de string
            String reponse = "{\"token\":\""+token+"\"}";

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(reponse);

        }
        catch (UserDoesNotExistException e){
            LOGGER.error("login exception UserDoesNotExistException");

            // Retourne une exception, car user existe déjà
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (InvalidPasswordException e){
            LOGGER.error("login exception InvalidPasswordException");

            // Retourne une exception, car user existe déjà
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Login doit dans login()
     * (pas de code métier dans le controller donc il faut appeler un service
     * login où il y a le code métier) :
     * - vérifier que login(email) et mdp sont bien entrée
     *      - si pas bien entrée - 400 bad request
     * - récupérer le mot de passe dans la bdd par rapport à l'email entré
     *      - si user non inscrit → 404 not found
     * - comparer le mot de passe de l'entrée avec celui de la bdd (bcrypt)
     *      - mdp.bdd = mdp.entrée → 200 ok + retourner token
     *      - mdp.bdd != mdp.entrée → 401 Unauthorized
     */
    // Endpoint pour gérer les requêtes d'inscription
    // @ApiOperation(value = "Register a new user", produces = "application/json")
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            // Appelle le service RegisterService pour effectuer l'inscription et récupère le token JWT résultant
            String token = authService.registerUser(registrationRequest);

            LOGGER.info("Register token created");

            /* objet json sous forme de string*/
            String reponse = "{\"token\":\""+token+"\"}";

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(reponse);

        } catch (UserAlreadyExistsException e) {
            LOGGER.error("register exception UserAlreadyExistsException");

            // Retourne une exception, car user existe déjà (400 -> ApiResponse)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Register doit dans register()
     * (pas de code métier dans le controller donc il faut appeler un service
     * register où il y a le code métier) :
     * - créer un user en base
     * - générer un token jwt
     * - retourner en retour de la requete le token jwt
     * La réponse doit être la même que la réponse de Mockoon
     */
    // @ApiOperation(value = "Get information about the current user", produces = "application/json")
    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<String> me(@CurrentSecurityContext SecurityContext context){
        try {
            // Pour récupérer l'user authentifié (avec token)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if(auth.getPrincipal().equals("anonymousUser")) {
                LOGGER.error("Utilisateur non connecté.");
                return ResponseEntity.ok().body("");
            }

            Integer userId = (Integer) auth.getPrincipal();

            // TODO : a supprimer
            LOGGER.info("Id récupérer : " + userId.toString());

            UserEntity reponseUser = authService.meUser(userId);

            return ResponseEntity.ok().body(reponseUser.infoMe());

        } catch (UserDoesNotExistException e){
            LOGGER.error("L'utilisateur n'existe pas.");

            // Retourne une exception, car user existe pas
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Erreur : " + e.getMessage());

            //500 - erreur côté server
            return ResponseEntity.internalServerError().body("");
        }

    }

    /**
     * Me doit dans me()
     * (pas de code métier dans le controller donc il faut appeler un service
     * register où il y a le code métier) :
     * - Principal = interface java spring qui représente les users connecter
     * -
     */

}
