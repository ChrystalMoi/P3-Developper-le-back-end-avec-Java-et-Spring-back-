package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.controllers.AuthController;
import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.exception.RentalDoesNotExistException;
import com.openclassrooms.projet3.repositories.RentalRepository;
import com.openclassrooms.projet3.request.RentalCreationRequest;
import com.openclassrooms.projet3.request.RentalUpdateRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private static final Logger logger = Logger.getLogger(AuthController.class);

    // --------------------------------------
    // Injection de dépendance du repository RentalRepository dans le service
    // --------------------------------------
    private RentalRepository rentalRepository;

    // --------------------------------------
    // Contrôleur
    // --------------------------------------
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

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

    // --------------------------------------
    // Méthode pour la vérification des entrées d'une requête
    // --------------------------------------
    private boolean areValidRequestRentalFields(String name, Double surface, Double price){
        logger.info("areValidRequestRentalFields (name.equals(\"\")) : " + name.equals(""));
        logger.info("areValidRequestRentalFields (name) : " + name);
        logger.info("areValidRequestRentalFields (surface) : " + surface);
        logger.info("areValidRequestRentalFields (price) : " + price);
        return surface != null && price != null && !name.equals("") && surface > 0 && price > 0;
    }

    // --------------------------------------
    // Méthode pour créer un bien en location
    // --------------------------------------
    public RentalEntity createRental(RentalCreationRequest rentalRequest) throws IllegalArgumentException {
        // Assure que les données requises sont fournies
        if (!areValidRequestRentalFields(rentalRequest.getName(), rentalRequest.getSurface(), rentalRequest.getPrice())) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        // Création de l'objet RentalEntity
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(rentalRequest.getName());
        rentalEntity.setPrice(rentalRequest.getPrice());
        rentalEntity.setPicture(rentalRequest.getPicture());
        rentalEntity.setSurface(rentalRequest.getSurface());
        rentalEntity.setDescription(rentalRequest.getDescription());
        rentalEntity.setOwnerId(1); // TODO - à supprimer

        // Enregistre le nouvel objet dans la base de données en utilisant la méthode du repository
        return rentalRepository.save(rentalEntity);
    }

    // --------------------------------------
    // Méthode pour update un bien en location
    // --------------------------------------
    public RentalEntity updateRental(RentalUpdateRequest rentalUpdateRequest, Integer id) throws IllegalArgumentException, RentalDoesNotExistException {
        // Assure que les données requises sont fournies
        if (!areValidRequestRentalFields(rentalUpdateRequest.getName(), rentalUpdateRequest.getSurface(), rentalUpdateRequest.getPrice())) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        // Extraction de l'objet RentalEntity avec l'id en entrée
        Optional<RentalEntity> rentalEntityUpdate = rentalRepository.findById(id);

        // Vérification que le Optional n'est pas vide
        if(rentalEntityUpdate.isEmpty()){
            throw new RentalDoesNotExistException("Le bien avec cet identifiant n'existe pas !");
        }

        // Extraction de RentalEntity de l'intérieur de l'Optional
        RentalEntity bddRental = rentalEntityUpdate.get();

        // Mise à jour du bien avec les nouvelles entrées
        bddRental.setName(rentalUpdateRequest.getName());
        bddRental.setPrice(rentalUpdateRequest.getPrice());
        bddRental.setSurface(rentalUpdateRequest.getSurface());
        bddRental.setDescription(rentalUpdateRequest.getDescription());

        // Enregistre le nouvel objet dans la base de données en utilisant la méthode du repository
        return rentalRepository.save(bddRental);
    }
}