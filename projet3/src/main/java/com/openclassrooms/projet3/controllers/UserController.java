package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
// @Api(tags = "User", description = "Endpoints related to Users")
public class UserController {
    // --------------------------------------
    // Injection de dépendance du service UserService dans le contrôleur
    // --------------------------------------
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --------------------------------------
    // Endpoint pour récupérer tous les utilisateurs
    // --------------------------------------
    // @ApiOperation(value = "Get all users", produces = "application/json")
    @GetMapping
    public List<UserEntity> getAllUsers() {
        // Appel à la méthode getAllUsers() du service UserService
        return userService.getAllUsers();
    }

    // --------------------------------------
    // Endpoint pour récupérer un utilisateur par son ID
    // Correspond à : /user/:id
    // --------------------------------------
    //@ApiOperation(value = "Get a user by ID", produces = "application/json")
    @GetMapping(value = "/{id}")
    public Optional<UserEntity> getUserById(@PathVariable Integer id) {
        // Appel à la méthode getUserById() du service UserService
        // Cette méthode utilise UserRepository pour récupérer un user par ID dans la base de donnée
        return userService.getUserById(id);
    }
}
