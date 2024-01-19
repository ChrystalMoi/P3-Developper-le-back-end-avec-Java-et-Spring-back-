package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.InvalidPasswordException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.repositories.UserRepository;
import com.openclassrooms.projet3.request.UserLoginRequest;
import com.openclassrooms.projet3.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
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

    private final UserRepository userRepository;

    // Composant pour générer des tokens JWT
    private final JwtTokenProvider jwtTokenProvider;

    // Utilisation du PasswordEncoder configuré dans SecurityConfig
    private final PasswordEncoder passwordEncoder;

    // Constructeur
    public LoginService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    //
    public String loginUser(UserLoginRequest userLoginRequest) throws UserDoesNotExistException, InvalidPasswordException{
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
                return jwtTokenProvider.generateToken(bddUser.getEmail());
            }
        }
    }
}
