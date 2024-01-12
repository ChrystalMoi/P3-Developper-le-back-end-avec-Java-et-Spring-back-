package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    // --------------------------------------
    // Injection de dépendance du repository RentalRepository dans le service
    // --------------------------------------
    @Autowired
    private RentalRepository rentalRepository;

    // --------------------------------------
    // Méthode pour récupérer tous les biens en location
    // --------------------------------------
    public List<RentalEntity> getAllRentals() {
        return rentalRepository.findAll();
    }

    // --------------------------------------
    // Méthode pour récupérer un bien en location par son ID
    // --------------------------------------
    public Optional<RentalEntity> getRentalById(Integer id) {
        return rentalRepository.findById(id);
    }
}