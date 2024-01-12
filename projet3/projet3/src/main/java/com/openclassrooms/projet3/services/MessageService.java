package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.MessageEntity;
import com.openclassrooms.projet3.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<MessageEntity> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }
}
