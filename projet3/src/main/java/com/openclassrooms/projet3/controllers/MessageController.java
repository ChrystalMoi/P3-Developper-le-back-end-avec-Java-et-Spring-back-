package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.MessageEntity;
import com.openclassrooms.projet3.request.MessageSendRequest;
import com.openclassrooms.projet3.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
//@Api(tags = "Messages", description = "Endpoints related to Messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //@ApiOperation(value = "Get all messages", produces = "application/json")
    @GetMapping
    public List<MessageEntity> getAllMessages() {
        return messageService.getAllMessages();
    }

    //@ApiOperation(value = "Send a new message", produces = "application/json")
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
            // En cas d'erreur de données requises non renseignées, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            // En cas d'autres erreurs, retourne une réponse avec le message d'erreur
            String errorResponse = "{\"message\": \"Une erreur s'est produite lors de la création du message.\"}";
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // @ApiOperation(value = "Get a message by ID", produces = "application/json")
    @GetMapping("/{id}")
    public Optional<MessageEntity> getMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id);
    }
}
