package com.openclassrooms.projet3.mappers;

import com.openclassrooms.projet3.dto.RentalDto;
import com.openclassrooms.projet3.entites.RentalEntity;

public class RentalMapper {

    public static RentalDto mapToDto(RentalEntity rentalEntity){
        return RentalDto.builder()
                .id(rentalEntity.getId())
                .name(rentalEntity.getName())
                .surface(rentalEntity.getSurface())
                .price(rentalEntity.getPrice())
                .picture(rentalEntity.getPicture())
                .description(rentalEntity.getDescription())
                .ownerId(rentalEntity.getOwnerId())
                .createdAt(rentalEntity.getCreatedAt())
                .updatedAt(rentalEntity.getUpdatedAt())
                .build();
    }

    public static RentalEntity mapToEntity(RentalDto rentalDto){
        return RentalEntity.builder()
                .id(rentalDto.getId())
                .name(rentalDto.getName())
                .surface(rentalDto.getSurface())
                .price(rentalDto.getPrice())
                .picture(rentalDto.getPicture())
                .description(rentalDto.getDescription())
                .ownerId(rentalDto.getOwnerId())
                .createdAt(rentalDto.getCreatedAt())
                .updatedAt(rentalDto.getUpdatedAt())
                .build();
    }
}
