package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.exception.RentalDoesNotExistException;
import com.openclassrooms.projet3.request.RentalCreationRequest;
import com.openclassrooms.projet3.request.RentalUpdateRequest;
import com.openclassrooms.projet3.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals Controller", description = "Endpoints related to Rentals")
public class RentalController {
    // --------------------------------------
    // Injection de dépendance du service RentalService
    // --------------------------------------
    private final RentalService rentalService;

    // --------------------------------------
    // Contrôleur
    // --------------------------------------
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // --------------------------------------
    // Endpoint pour récupérer tous les biens en location
    // Correspond à : /rentals
    // Méthode GET
    // --------------------------------------
    @Operation(
            summary = "Get All Rentals",
            description = "Retrieves a list of all rental entities.",
            tags = { "Rental" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of rentals.", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RentalEntity.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "text/plain") })
    })
    @GetMapping
    public List<RentalEntity> getAllRentals() {
        // Appel à la méthode de RentalService
        return rentalService.getAllRentals();
    }

    // --------------------------------------
    // Endpoint pour créer un bien de location
    // Correspond à : /rentals
    // Méthode POST
    // --------------------------------------
    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental entity.",
            tags = { "Rental" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created the rental.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request. Check if all required fields are provided.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "text/plain") })
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<String> createRental(@ModelAttribute RentalCreationRequest rentalRequest, Principal principal) {
        try {
            // Enregistre le nouvel objet dans la base de données en utilisant la méthode du service
            RentalEntity createdRental = rentalService.createRental(rentalRequest, principal.getName());

            // Crée un message JSON avec le message de succès
            String jsonResponse = "{\"message\": \"Rental created !\"}";

            // Retourne un ResponseEntity avec le message JSON de succès
            return ResponseEntity.ok(jsonResponse);

        } catch (IllegalArgumentException e) {
            // En cas d'erreur de données requises non renseignées, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            // En cas d'autres erreurs, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"Une erreur s'est produite lors de la création du bien locatif.\"}";
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * CreateRentals doit dans createRental()
     * (pas de code métier dans le controller donc il faut appeler un service où il y a le code métier) :
     * - créer un bien en base
     * - retourner en retour de la requete un message : Rental created !
     * La réponse doit être la même que la réponse de Mockoon
     */

    // --------------------------------------
    // Endpoint pour récupérer un bien en location par son ID
    // Correspond à : /rentals/:id
    // Méthode GET
    // --------------------------------------
    @Operation(
            summary = "Get Rental by ID",
            description = "Retrieves a rental entity by its ID.",
            tags = { "Rental" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the rental.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Rental not found.", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public Optional<RentalEntity> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id);
    }

    // --------------------------------------
    // Endpoint pour mettre à jour (update) un bien en location par son ID
    // Correspond à : /rentals/:id
    // Méthode PUT
    // @ModelAttribute = multipart (form-data avec différents type de variable)
    // --------------------------------------
    @Operation(
            summary = "Update Rental by ID",
            description = "Updates a rental entity by its ID.",
            tags = { "Rental" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the rental.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request. Check the request parameters.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Rental not found.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "application/json") })
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data" ,produces = "application/json")
    public ResponseEntity updateRental(@PathVariable Integer id, @ModelAttribute RentalUpdateRequest rentalUpdateRequest){
        System.out.println("avant le try du update");

        try {
            RentalEntity rentalEntity = rentalService.updateRental(rentalUpdateRequest, id);

            // Crée un message JSON avec le message de succès
            String jsonResponse = "{\"message\": \"Rental updated !\"}";

            return ResponseEntity.ok().body(jsonResponse);

        } catch (IllegalArgumentException e){
            String errorResponse = "{\"message\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorResponse);

        } catch(RentalDoesNotExistException e){
            String errorResponse = "{\"message\": \" "+ e.getMessage() + "\"}";
            return ResponseEntity.status(404).body(errorResponse);

        } catch(Exception e){
            String errorResponse = "{\"message\": \"Une erreur s'est produite lors de la modification du bien locatif.\"}";
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
