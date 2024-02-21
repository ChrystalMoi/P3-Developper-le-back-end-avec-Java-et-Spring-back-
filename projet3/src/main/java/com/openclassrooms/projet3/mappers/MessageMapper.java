package com.openclassrooms.projet3.mappers;

import com.openclassrooms.projet3.dto.MessageDto;
import com.openclassrooms.projet3.entites.MessageEntity;

public abstract class MessageMapper {

    public static MessageDto mapToDto(MessageEntity message){

        return MessageDto.builder()
                .id(message.getId())
                .rentalId(message.getRentalId())
                .userId(message.getUserId())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    public static MessageEntity mapToEntity(MessageDto messageDto){

        return MessageEntity.builder()
                .id(messageDto.getId())
                .rentalId(messageDto.getRentalId())
                .userId(messageDto.getUserId())
                .message(messageDto.getMessage())
                .createdAt(messageDto.getCreatedAt())
                .updatedAt(messageDto.getUpdatedAt())
                .build();

    }
}
