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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    // Composant pour générer des tokens JWT
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    // Utilisation du PasswordEncoder configuré dans SecurityConfig
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserRegistrationRequest registrationRequest) throws UserAlreadyExistsException{
        // Vérification si l'utilisateur existe déjà dans la base de données (ici par email)
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("L'utilisateur avec ce mail existe déjà.");
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
