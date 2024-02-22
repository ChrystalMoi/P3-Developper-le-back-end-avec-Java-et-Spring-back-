package com.openclassrooms.projet3.mappers;

import com.openclassrooms.projet3.dto.UserDto;
import com.openclassrooms.projet3.entites.UserEntity;

public abstract class UserMapper {

    public static UserDto mapToDto(UserEntity user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserEntity mapToEntity(UserDto userDto){
        return UserEntity.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .build();
    }
}
