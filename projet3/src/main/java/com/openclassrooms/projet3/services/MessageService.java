package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.MessageEntity;
import com.openclassrooms.projet3.repositories.MessageRepository;
import com.openclassrooms.projet3.request.MessageSendRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<MessageEntity> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    /**
     * Envoie un message enregistré par l'utilisateur pour une location spécifique.
     *
     * @param messageSendRequest Les détails du message à envoyer.
     * @return L'entité du message créé et enregistré dans la base de données.
     * @throws IllegalArgumentException Si toutes les informations requises ne sont pas fournies.
     */
    public MessageEntity envoieMessages(MessageSendRequest messageSendRequest) throws IllegalArgumentException {
        // Assure que les données requises sont fournies
        if (messageSendRequest.getMessage().equals("") || messageSendRequest.getUserId() == null || messageSendRequest.getRentalId() == null) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        // Création de l'objet MessageEntity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(messageSendRequest.getMessage());
        messageEntity.setUserId((messageSendRequest.getUserId()));
        messageEntity.setRentalId((messageSendRequest.getRentalId()));

        // Enregistre le nouveau message objet dans la base de données en utilisant la méthode du repository
        return messageRepository.save(messageEntity);
    }
}
