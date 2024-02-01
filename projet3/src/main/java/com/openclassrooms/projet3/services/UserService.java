package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // --------------------------------------
    // Injection de dépendance de UserRepository dans UserService
    // --------------------------------------
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --------------------------------------
    // Méthode pour récupérer tous les utilisateurs
    // --------------------------------------
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // --------------------------------------
    // Méthode pour récupérer un utilisateur par son ID
    // --------------------------------------
    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }
}
