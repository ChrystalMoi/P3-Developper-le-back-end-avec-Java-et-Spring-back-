package com.openclassrooms.projet3.repositories;

import com.openclassrooms.projet3.entites.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
}
