package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.MessageEntity;
import com.openclassrooms.projet3.request.MessageSendRequest;
import com.openclassrooms.projet3.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages Controller", description = "Endpoints related to Messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Opération pour obtenir tous les messages. <br>
     * Cette méthode GET permet de récupérer une liste de tous les messages.
     *
     * @return List<MessageEntity> - Liste des messages récupérés.
     * @throws Exception En cas d'erreur interne du serveur.
     */
    @Operation(
            summary = "Get All Messages",
            description = "Retrieves a list of all messages.",
            tags = { "Message" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of messages.", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageEntity.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping
    public List<MessageEntity> getAllMessages() {
        return messageService.getAllMessages();
    }

    /**
     * Opération pour envoyer un nouveau message.  <br>
     * Cette méthode POST gère les requêtes pour envoyer un message.
     *
     * @param messageSendRequest L'objet de requête contenant les informations sur le message à envoyer.
     * @return ResponseEntity - Une réponse HTTP indiquant le succès ou l'échec de l'envoi du message.
     * @throws IllegalArgumentException Si les données requises ne sont pas renseignées correctement.
     */
    @Operation(
            summary = "Send a new Message",
            description = "Handles requests to send a message.",
            tags = { "Message" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message sent successfully.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request or invalid data.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(mediaType = "application/json") })
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity envoieMessages(@RequestBody MessageSendRequest messageSendRequest){
        try {
            // Enregistre le nouvel objet dans la base de données en utilisant la méthode du service
            MessageEntity createdMessage = messageService.envoieMessages(messageSendRequest);

            // Crée un message JSON avec le message de succès
            String jsonResponse = "{\"message\": \"Message send with success\"}";

            // Retourne un ResponseEntity avec le message JSON de succès
            return ResponseEntity.ok(jsonResponse);

        } catch (IllegalArgumentException e) {
            // En cas d'erreur de données requises non renseignées,
            // retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            // En cas d'autres erreurs, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"Une erreur s'est produite lors de la création du message.\"}";
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Opération pour obtenir un message par son identifiant. <br>
     * Cette méthode GET gère les requêtes pour récupérer un message par son identifiant.
     *
     * @param id L'identifiant du message à récupérer.
     * @return Optional<MessageEntity> - L'objet représentant le message récupéré, s'il existe.
     */
    @Operation(
            summary = "Get Message by ID",
            description = "Handles requests to retrieve a message by its ID.",
            tags = { "Message" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message retrieved successfully.", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Message not found.", content = { @Content(mediaType = "application/json") })
    })
    @GetMapping("/{id}")
    public Optional<MessageEntity> getMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id);
    }
}
