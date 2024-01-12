package com.openclassrooms.projet3.repositories;

import com.openclassrooms.projet3.entites.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
