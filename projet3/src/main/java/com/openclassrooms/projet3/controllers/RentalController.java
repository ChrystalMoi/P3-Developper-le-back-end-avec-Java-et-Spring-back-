package com.openclassrooms.projet3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.exception.ImageNotFoundException;
import com.openclassrooms.projet3.exception.RentalDoesNotExistException;
import com.openclassrooms.projet3.request.RentalCreationRequest;
import com.openclassrooms.projet3.request.RentalUpdateRequest;
import com.openclassrooms.projet3.response.RentalResponse;
import com.openclassrooms.projet3.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals Controller", description = "Endpoints related to Rentals")
//@SecurityRequirement(name = "bearerAuth")
public class RentalController {
    private static final Logger LOGGER = Logger.getLogger(RentalController.class);

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

    /**
     * Endpoint pour obtenir toutes les locations. <br>
     * Cette méthode GET permet de récupérer une liste de toutes les entités de location. <br>
     * Correspond à : /rentals
     *
     * @return ResponseEntity<String> - Réponse HTTP contenant la liste des locations au format
     * JSON en cas de succès. <br>
     * En cas d'erreur interne du serveur, une réponse avec un code d'erreur approprié est renvoyée.
     */
    @Operation(
            summary = "Get All Rentals",
            description = "Retrieves a list of all rental entities.",
            tags = { "Rental" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of rentals.", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RentalEntity.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "text/plain") })
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<String> getAllRentals() {
        try{
            List<RentalResponse> listGetAll = rentalService.getAllRentals();

            String response = new ObjectMapper().writeValueAsString(listGetAll);

            String jsonResponse = "{\"rentals\": " + response +"}";

            // Appel à la méthode de RentalService
            return ResponseEntity.ok().body(jsonResponse);
        }
        catch (ImageNotFoundException e) {
            LOGGER.error("ImageNotFoundException : " + e);
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            LOGGER.error("Exception : "+ e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint pour créer un bien de location. <br>
     * Cette méthode POST permet de créer une nouvelle entité de location. <br>
     * Correspond à : /rentals
     * @param rentalRequest     Les détails de la demande de création de location.
     * @param principal         L'utilisateur principal effectuant la demande.
     * @return ResponseEntity<String> Une réponse HTTP indiquant le succès ou l'échec de la création de la location. <br>
     *                                 En cas de succès, un message JSON est retourné indiquant que la location a été créée avec succès. <br>
     *                                 En cas d'erreur, une réponse avec un code d'erreur approprié est renvoyée, accompagnée d'un message d'erreur.
     */
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
            LOGGER.error("IllegalArgumentException : " + e);
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            // En cas d'autres erreurs, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"Une erreur s'est produite lors de la création du bien locatif.\"}";
            LOGGER.error("Exception : "+ e);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Endpoint pour récupérer un bien en location par son ID. <br>
     * Cette méthode GET permet de récupérer une entité de location par son identifiant. <br>
     * Correspond à : /rentals/:id
     * @param id L'identifiant du bien en location à récupérer.
     * @return Optional<RentalEntity> - L'objet représentant le bien en location récupéré, s'il existe. <br>
     *                                Si aucun bien en location correspondant à l'ID donné n'est trouvé, Optional.empty() est retourné.
     */
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

    /**
     * Endpoint pour mettre à jour (update) un bien en location par son ID. <br>
     * Cette méthode PUT permet de mettre à jour une entité de location par son identifiant. <br>
     *
     * Correspond à : /rentals/:id
     * @ModelAttribute = multipart (form-data avec différents types de variables)
     *
     * @param id L'identifiant du bien en location à mettre à jour.
     * @param rentalUpdateRequest Les détails de la demande de mise à jour du bien en location.
     *
     * @return ResponseEntity - Une réponse HTTP indiquant le succès ou l'échec de la mise à jour du bien en location. <br>
     *                        En cas de succès, un message JSON est retourné indiquant que la location a été mise à jour avec succès. <br>
     *                        En cas d'erreur, une réponse avec un code d'erreur approprié est renvoyée, accompagnée d'un message d'erreur.
     */
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
