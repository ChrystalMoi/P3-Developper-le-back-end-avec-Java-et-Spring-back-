package com.openclassrooms.projet3.controllers;

import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    // --------------------------------------
    // Injection de dépendance du service RentalService dans le contrôleur
    // --------------------------------------
    @Autowired
    private RentalService rentalService;

    // --------------------------------------
    // Endpoint pour récupérer tous les biens en location
    // --------------------------------------
    @GetMapping
    public List<RentalEntity> getAllRentals() {
        // Appell à la méthode de RentalService
        return rentalService.getAllRentals();
    }

    // --------------------------------------
    // Endpoint pour récupérer un bien en location par son ID
    // --------------------------------------
    @GetMapping("/{id}")
    public Optional<RentalEntity> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id);
    }
}
