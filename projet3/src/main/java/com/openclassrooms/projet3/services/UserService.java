package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.dto.UserDto;
import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.mappers.UserMapper;
import com.openclassrooms.projet3.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // --------------------------------------
    // Méthode pour récupérer un utilisateur par son ID
    // --------------------------------------
    public UserDto getUserById(Integer id) throws Exception{
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("User not found"));
        return UserMapper.mapToDto(userEntity);
    }
}
