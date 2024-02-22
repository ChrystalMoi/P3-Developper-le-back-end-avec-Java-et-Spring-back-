package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.dto.UserDto;
import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "Endpoints related to Authentification")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class);

    // Injection de dépendance du service RegisterService et loginService dans le contrôleur
    private final AuthService authService;

    //Constructeur qui remplace l'autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint pour gérer les requêtes de connexion.  <br>
     * Cette méthode POST gère les requêtes de connexion utilisateur et renvoie un jeton JWT en cas de connexion réussie.
     * @param userLoginRequest L'objet de requête contenant les informations de connexion de l'utilisateur.
     * @return ResponseEntity<String> - Réponse HTTP contenant le jeton JWT dans le corps de la réponse en cas de succès. <br>
     * En cas d'erreur, une réponse appropriée est renvoyée avec un message d'erreur.
     */
    @Operation(
            summary = "User Login",
            description = "Handles user login requests and returns a JWT token upon successful login.",
            tags = { "Authentication" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged in and returns a JWT token.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "User does not exist.", content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "401", description = "Invalid password.", content = { @Content(mediaType = "text/plain") })
    })
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) {
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (InvalidPasswordException e){
            LOGGER.error("login exception InvalidPasswordException");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint pour gérer les requêtes d'inscription.  <br>
     * Cette méthode POST gère les requêtes d'inscription utilisateur et renvoie un jeton JWT en cas d'inscription réussie.
     * @param registrationRequest L'objet de requête contenant les informations d'inscription de l'utilisateur.
     * @return ResponseEntity<String> - Réponse HTTP contenant le jeton JWT dans le corps de la réponse en cas d'inscription réussie. <br>
     * En cas d'échec (par exemple, si l'utilisateur existe déjà), une réponse appropriée est renvoyée avec un message d'erreur.
     */
    @Operation(
            summary = "User Registration",
            description = "Handles user registration requests and returns a JWT token upon successful registration.",
            tags = { "Authentication" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registered and returns a JWT token.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "User already exists.", content = { @Content(mediaType = "text/plain") })
    })
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registrationRequest) {
        try {
            // Appelle le service RegisterService pour effectuer l'inscription et récupère le token JWT résultant
            String token = authService.registerUser(registrationRequest);

            LOGGER.info("Register token created");

            String reponse = "{\"token\":\""+token+"\"}";

            // Retourne une réponse HTTP avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok().body(reponse);

        } catch (UserAlreadyExistsException e) {
            LOGGER.error("register exception UserAlreadyExistsException");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Opération pour obtenir les détails de l'utilisateur.  <br>
     * Cette méthode GET permet de récupérer les détails de l'utilisateur authentifié.
     * @param context Le contexte de sécurité actuel pour l'utilisateur authentifié.
     * @return ResponseEntity<String> Réponse HTTP contenant les détails de l'utilisateur au format JSON en cas de succès.  <br>
     * En cas d'erreur (par exemple, si l'utilisateur n'existe pas, n'est pas authentifié ou s'il y a une erreur interne du serveur),
     * une réponse appropriée avec un message d'erreur est renvoyée.
     */
    @Operation(
            summary = "Get User Details",
            description = "Retrieves details of the authenticated user.",
            tags = { "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user details.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "User does not exist or is not authenticated.", content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "text/plain") })
    })
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

            UserDto reponseUser = authService.meUser(userId);

            return ResponseEntity.ok().body(reponseUser.infoMe());

        } catch (UserDoesNotExistException e){
            LOGGER.error("L'utilisateur n'existe pas.");
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            LOGGER.error("Erreur : " + e.getMessage());
            return ResponseEntity.internalServerError().body("");
        }
    }
}
