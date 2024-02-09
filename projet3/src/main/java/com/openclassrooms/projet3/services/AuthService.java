package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.repositories.UserRepository;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
     * Enregistre un nouvel utilisateur dans la base de données. <br>
     *
     * Vérifie d'abord si l'utilisateur n'existe pas déjà dans la base de données en vérifiant l'unicité de son adresse e-mail. <br>
     * Si l'utilisateur existe déjà, une exception UserAlreadyExistsException est levée. <br>
     * Vérifie également que toutes les informations nécessaires (adresse e-mail, nom et mot de passe) sont fournies dans la demande d'inscription. <br> <br>
     * Si des informations sont manquantes, une IllegalArgumentException est levée. <br>
     * Crée ensuite un nouvel utilisateur avec les informations fournies dans la demande d'inscription, encode le mot de passe,
     * et enregistre l'utilisateur dans la base de données avec la date et l'heure actuelles. <br>
     * Enfin, génère un token JWT pour l'utilisateur nouvellement créé et le renvoie. <br>
     *
     * @param registrationRequest La demande d'inscription contenant les informations de l'utilisateur à enregistrer.
     * @return String - Le token JWT généré pour l'utilisateur nouvellement enregistré.
     * @throws UserAlreadyExistsException Si un utilisateur avec la même adresse e-mail existe déjà dans la base de données.
     * @throws IllegalArgumentException Si des informations requises (adresse e-mail, nom ou mot de passe) sont manquantes dans la demande d'inscription.
     */
    @Transactional
    public String registerUser(UserRegistrationRequest registrationRequest) throws UserAlreadyExistsException {
        // Vérification si l'utilisateur existe déjà dans la base de données (ici par email)
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("L'utilisateur avec ce mail existe déjà.");
        }

        // Vérification que toutes les informations nécessaires sont renseignées
        if (registrationRequest.getEmail() == null || registrationRequest.getName() == null || registrationRequest.getPassword() == null) {
            throw new IllegalArgumentException("Toutes les informations (email, nom et mot de passe) sont requises. Merci de tout renseigner.");
        }

        Timestamp temps = Timestamp.valueOf(LocalDateTime.now());

        // Crée un nouvel utilisateur via UserEntity
        UserEntity user = new UserEntity();
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setCreatedAt(temps);
        user.setUpdatedAt(temps);

        // Enregistre l'utilisateur dans la base de données
        userRepository.save(user);

        // Génère un token JWT uniquement si l'user est créable
        return jwtTokenProvider.generateToken(user);
    }

    /**
     * Authentifie un utilisateur avec les informations de connexion fournies.
     *
     * @param userLoginRequest Les informations de connexion de l'utilisateur.
     * @return Le token JWT généré pour l'utilisateur authentifié.
     * @throws UserDoesNotExistException Si l'utilisateur n'existe pas dans la base de données.
     * @throws InvalidPasswordException Si le mot de passe fourni est invalide.
     */
    public String loginUser(UserLoginRequest userLoginRequest) throws UserDoesNotExistException, InvalidPasswordException {
        // S'il n'y a pas d'entrée avec l'email dans la bdd alors lancement de l'exception
        if (!userRepository.existsByEmail(userLoginRequest.getEmail())) {
            throw new UserDoesNotExistException("L'utilisateur avec ce mail n'existe pas.");
        }

        // Vérification si l'utilisateur existe déjà dans la base de données (ici par email=login)
        else{
            // Récupérer l'objet user qui correspond au mail (en utilisant userRepository)
            Optional<UserEntity> optionalBddUser = userRepository.findByEmail(userLoginRequest.getEmail());

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

    /**
     * Permet de donner les informations de l'utilisateur connecté
     *
     * @param userId
     * @return UserEntity - L'utilisateur avec l'id du paramètre.
     * @throws UserDoesNotExistException - si l'utilisateur n'est pas dans la base de données.
     */
    public UserEntity meUser(Integer userId) throws UserDoesNotExistException{
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User introuvable"));
    }
}
