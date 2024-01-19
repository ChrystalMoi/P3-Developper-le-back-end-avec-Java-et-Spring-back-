package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.UserAlreadyExistsException;
import com.openclassrooms.projet3.repositories.UserRepository;
import com.openclassrooms.projet3.request.UserRegistrationRequest;
import com.openclassrooms.projet3.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    // Composant pour générer des tokens JWT
    private final JwtTokenProvider jwtTokenProvider;

    // Utilisation du PasswordEncoder configuré dans SecurityConfig
    private final PasswordEncoder passwordEncoder;

    // Constructeur
    public RegisterService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserRegistrationRequest registrationRequest) throws UserAlreadyExistsException{
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
        return jwtTokenProvider.generateToken(user.getEmail());
    }
}
