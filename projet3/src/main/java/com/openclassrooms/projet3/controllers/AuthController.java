package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.services.AuthService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class);

    // Injection de dépendance du service RegisterService et loginService dans le contrôleur
    private final AuthService authService;


    //Constructeur qui remplace l'autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint pour gérer les requêtes de connexion
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        logger.info("login ok");

        try{
            // Appelle le service LoginService pour effectuer la connexion et récupère le token JWT résultant
            String token = authService.loginUser(userLoginRequest);

            logger.info("login token created");

            // objet json sous forme de string
            String reponse = "{\"token\":\""+token+"\"}";

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(reponse);

        }
        catch (UserDoesNotExistException e){
            logger.error("login exception UserDoesNotExistException");

            // Retourne une exception, car user existe déjà
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (InvalidPasswordException e){
            logger.error("login exception InvalidPasswordException");

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
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registrationRequest) {
        // Affiche un message de confirmation dans la console
        logger.info("dans le register");

        try {
            // Appelle le service RegisterService pour effectuer l'inscription et récupère le token JWT résultant
            String token = authService.registerUser(registrationRequest);

            logger.info("register token created");

            /* objet json sous forme de string*/
            String reponse = "{\"token\":\""+token+"\"}";

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(reponse);

        } catch (UserAlreadyExistsException e) {
            logger.error("register exception UserAlreadyExistsException");

            // Retourne une exception, car user existe déjà
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

    @GetMapping("/me")
    public void me() {
        logger.info("me ok");

        // Faire la sécurisation avant (demander à Joachim en quoi cela consiste)

        // Vérifier que "/me" retourne la même chose que Mockoon sans token
        // (dans Authorization -> No Auth)
    }

}
