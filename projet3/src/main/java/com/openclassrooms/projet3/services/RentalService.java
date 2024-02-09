package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.ImageNotFoundException;
import com.openclassrooms.projet3.exception.RentalDoesNotExistException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.repositories.RentalRepository;
import com.openclassrooms.projet3.repositories.UserRepository;
import com.openclassrooms.projet3.request.RentalCreationRequest;
import com.openclassrooms.projet3.request.RentalUpdateRequest;
import com.openclassrooms.projet3.response.RentalResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private static final Logger logger = Logger.getLogger(RentalService.class);

    // --------------------------------------
    // Injection de dépendances dans le service
    // --------------------------------------
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    // --------------------------------------
    // Contrôleur
    // --------------------------------------
    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, ImageService imageService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    // --------------------------------------
    // Méthode pour récupérer tous les biens en location
    // --------------------------------------
    public List<RentalResponse> getAllRentals() throws IOException, ImageNotFoundException {
        // On stock la liste des rentals entities dans une variable
        List<RentalEntity> rentalEntities = rentalRepository.findAll();

        List<RentalResponse> rentalResponses = new ArrayList<>();

        // Pour chaque variable rentalEntity de type RentalEntity dans la liste rentalEntities
        for (RentalEntity rentalEntity : rentalEntities) {
            // Récupération image du dossier
            //String image = imageService.getImage(rentalEntity.getPicture());

            // Création de l'objet rentalResponse
            RentalResponse rentalResponse = new RentalResponse(
                    rentalEntity.getId(),
                    rentalEntity.getName(),
                    rentalEntity.getSurface(),
                    rentalEntity.getPrice(),
                    rentalEntity.getPicture(),
                    rentalEntity.getDescription(),
                    rentalEntity.getOwnerId(),
                    rentalEntity.getCreatedAt(),
                    rentalEntity.getUpdatedAt());

            // Ajoute l'objet rentalResponse à la liste RentalResponses
            rentalResponses.add(rentalResponse);
        }

        return rentalResponses;
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
        return surface != null && price != null && !name.equals("") && surface > 0 && price > 0;
    }

    // --------------------------------------
    // Méthode pour créer un bien en location
    // --------------------------------------
    public RentalEntity createRental(RentalCreationRequest rentalRequest, String currentUserId) throws IllegalArgumentException, IOException {
        // Assure que les données requises sont fournies
        if (!areValidRequestRentalFields(rentalRequest.getName(), rentalRequest.getSurface(), rentalRequest.getPrice())) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        UserEntity currentUser = userRepository.findById(Integer.valueOf(currentUserId))
                .orElseThrow(()->new UserDoesNotExistException("User does not exist."));

        // On récupère le fichier image de la requête et on l'enregistre dans le dossier
        String image = imageService.sauvegardeImageDansDossier(rentalRequest.getPicture());

        // Création de l'objet RentalEntity
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(rentalRequest.getName());
        rentalEntity.setPrice(rentalRequest.getPrice());
        rentalEntity.setPicture("https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg");
        rentalEntity.setSurface(rentalRequest.getSurface());
        rentalEntity.setDescription(rentalRequest.getDescription());
        rentalEntity.setOwnerId(currentUser.getId());

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

        // ligne 88 à 96 -> RentalEntity bddRental = rentalRepository.findById(id).orElseThrow(()-> new RentalDoesNotExistException("Le bien avec cet identifiant n'existe pas !"));

        // Mise à jour du bien avec les nouvelles entrées
        bddRental.setName(rentalUpdateRequest.getName());
        bddRental.setPrice(rentalUpdateRequest.getPrice());
        bddRental.setSurface(rentalUpdateRequest.getSurface());
        bddRental.setDescription(rentalUpdateRequest.getDescription());

        // Enregistre le nouvel objet dans la base de données en utilisant la méthode du repository
        return rentalRepository.save(bddRental);
    }
}