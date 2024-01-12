package com.openclassrooms.projet3.repositories;

import com.openclassrooms.projet3.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // Les méthodes findAll et findById sont générées automatiquement par Spring Data JPA

    boolean existsByEmail(String email);
}
