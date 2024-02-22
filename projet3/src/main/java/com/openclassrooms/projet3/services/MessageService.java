package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.dto.MessageDto;
import com.openclassrooms.projet3.entites.MessageEntity;
import com.openclassrooms.projet3.mappers.MessageMapper;
import com.openclassrooms.projet3.repositories.MessageRepository;
import com.openclassrooms.projet3.request.MessageSendRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(MessageMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public MessageDto getMessageById(Integer id) throws Exception {
        MessageEntity messageEntity = messageRepository.findById(id).orElseThrow(()->new Exception("Message not found"));
        return MessageMapper.mapToDto(messageEntity);
    }

    /**
     * Envoie un message enregistré par l'utilisateur pour une location spécifique.
     *
     * @param messageSendRequest Les détails du message à envoyer.
     * @return L'entité du message créé et enregistré dans la base de données.
     * @throws IllegalArgumentException Si toutes les informations requises ne sont pas fournies.
     */
    public MessageDto envoieMessages(MessageSendRequest messageSendRequest) throws IllegalArgumentException {
        // Assure que les données requises sont fournies
        if (messageSendRequest.getMessage().equals("") || messageSendRequest.getUserId() == null || messageSendRequest.getRentalId() == null) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        // Création de l'objet MessageEntity
        MessageDto messageDto = MessageDto.builder()
                .rentalId(messageSendRequest.getRentalId())
                .userId(messageSendRequest.getUserId())
                .message(messageSendRequest.getMessage())
                .build();

        MessageEntity messageEntity = MessageMapper.mapToEntity(messageDto);

        MessageEntity response = messageRepository.save(messageEntity);

        return MessageMapper.mapToDto(response);
    }
}
