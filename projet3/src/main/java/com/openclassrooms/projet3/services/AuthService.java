package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.repositories.UserRepository;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Composant pour générer des tokens JWT
    private final JwtTokenProvider jwtTokenProvider;

    // Utilisation du PasswordEncoder configuré dans SecurityConfig
    private final PasswordEncoder passwordEncoder;

    private final JwtDecoder jwtDecoder;

    // Controller
    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
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
    public String registerUser(UserRegistrationRequest registrationRequest) throws UserAlreadyExistsException {
        // Vérification si l'utilisateur existe déjà dans la base de données (ici par email)
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("L'utilisateur avec ce mail existe déjà.");
        }

        // Vérification que toutes les informations nécessaires sont renseignées
        if (registrationRequest.getEmail() == null || registrationRequest.getName() == null || registrationRequest.getPassword() == null) {
            throw new IllegalArgumentException("Toutes les informations (email, nom et mot de passe) sont requises. Merci de tout renseigner.");
        }

        // Crée un nouvel utilisateur via UserEntity
        UserEntity user = new UserEntity();
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        // Enregistre l'utilisateur dans la base de données
        userRepository.save(user);

        // Génère un token JWT
        return jwtTokenProvider.generateToken(user);
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
    public String loginUser(UserLoginRequest userLoginRequest) throws UserDoesNotExistException, InvalidPasswordException {
        // S'il n'y a pas d'entrée avec l'email dans la bdd alors lancement de l'exception
        if (!userRepository.existsByEmail(userLoginRequest.getLogin())) {
            throw new UserDoesNotExistException("L'utilisateur avec ce mail n'existe pas.");
        }

        // Vérification si l'utilisateur existe déjà dans la base de données (ici par email=login)
        else{
            // Récupérer l'objet user qui correspond au mail (en utilisant userRepository)
            Optional<UserEntity> optionalBddUser = userRepository.findByEmail(userLoginRequest.getLogin());

            // Vérification que le Optional n'est pas vide
            if(optionalBddUser.isEmpty()){
                throw new UserDoesNotExistException("L'utilisateur avec ce mail n'existe pas.");
            }

            // Extraction de UserEntity de l'intérieur de l'Optional
            UserEntity bddUser = optionalBddUser.get();

            // Vérification mdp.bdd = mdp.entrée
            boolean matchPasswords = passwordEncoder.matches(userLoginRequest.getPassword(), bddUser.getPassword());

            // Si les mots de passe ne sont pas identique alors Exception
            if (!matchPasswords) {
                throw new InvalidPasswordException("Mot de passe invalide.");
            }

            //Si les mots de passe sont identiques alors connexion
            else {
                // Génère un token JWT
                return jwtTokenProvider.generateToken(bddUser);
            }
        }
    }

    public UserEntity meUser(Integer userId) throws UserDoesNotExistException{
        // Dans idToken, on stocke l'utilisateur avec le même id que celui du claim
        //int idToken = Integer.parseInt(token.getClaimAsString("id"));

        // On cherche dans la bdd un user avec le même id que celui du token
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            throw new UserDoesNotExistException("User introuvable");
        } else {
            return optionalUser.get();
        }
    }
}
