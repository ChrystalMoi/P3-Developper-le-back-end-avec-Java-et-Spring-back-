package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "Endpoints related to User")
public class UserController {
    // --------------------------------------
    // Injection de dépendance du service UserService dans le contrôleur
    // --------------------------------------
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint pour récupérer tous les utilisateurs. <br>
     * Cette méthode GET permet de récupérer une liste de tous les utilisateurs.
     *
     * @return List<UserEntity> - La liste des utilisateurs récupérés. <br>
     * Si aucun utilisateur n'est trouvé, une liste vide est retournée.
     */
    @Operation(
            summary = "Get All Users",
            description = "Handles requests to retrieve a list of all users.",
            tags = { "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "No users found.", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Endpoint pour récupérer un utilisateur par son ID. <br>
     * Cette méthode GET permet de récupérer un utilisateur par son identifiant. <br>
     *
     * Correspond à : /user/:id
     *
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return Optional<UserEntity> - L'objet représentant l'utilisateur récupéré, s'il existe. <br>
     *                              Si aucun utilisateur correspondant à l'ID donné n'est trouvé, Optional.empty() est retourné.
     */
    @Operation(
            summary = "Get User by ID",
            description = "Handles requests to retrieve a user by their ID.",
            tags = { "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User not found.", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping(value = "/{id}")
    public Optional<UserEntity> getUserById(@PathVariable Integer id) {
        // Appel à la méthode getUserById() du service UserService
        // Cette méthode utilise UserRepository pour récupérer un user par ID dans la base de donnée
        return userService.getUserById(id);
    }
}
