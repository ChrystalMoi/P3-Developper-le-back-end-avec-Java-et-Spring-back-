package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.dto.RentalDto;
import com.openclassrooms.projet3.dto.UserDto;
import com.openclassrooms.projet3.entites.RentalEntity;
import com.openclassrooms.projet3.entites.UserEntity;
import com.openclassrooms.projet3.exception.ImageNotFoundException;
import com.openclassrooms.projet3.exception.RentalDoesNotExistException;
import com.openclassrooms.projet3.exception.UserDoesNotExistException;
import com.openclassrooms.projet3.mappers.RentalMapper;
import com.openclassrooms.projet3.mappers.UserMapper;
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
import java.util.stream.Collectors;

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
    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(RentalMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // --------------------------------------
    // Méthode pour récupérer un bien en location par son ID
    // --------------------------------------
    public RentalDto getRentalById(Integer id) {
        RentalEntity rentalEntity = rentalRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Rental not found"));
        return RentalMapper.mapToDto(rentalEntity);
    }

    /**
     * Vérifie si les champs requis d'une requête de location sont valides.
     *
     * @param name    Le nom du bien en location.
     * @param surface La surface du bien en location.
     * @param price   Le prix du bien en location.
     * @return true si tous les champs requis sont valides, sinon false.
     */
    private boolean areValidRequestRentalFields(String name, Double surface, Double price){
        return surface != null && price != null && !name.equals("") && surface > 0 && price > 0;
    }

    /**
     * Crée un nouveau bien en location avec les détails fournis.
     *
     * @param rentalRequest Les détails du bien en location à créer.
     * @param currentUserId L'identifiant de l'utilisateur actuel.
     * @return L'entité du bien en location créé et enregistré dans la base de données.
     * @throws IllegalArgumentException Si toutes les informations requises ne sont pas fournies.
     * @throws IOException En cas d'erreur lors de l'accès ou de la manipulation d'un fichier.
     * @throws UserDoesNotExistException Si l'utilisateur actuel n'existe pas dans la base de données.
     */
    public RentalEntity createRental(RentalCreationRequest rentalRequest, String currentUserId) throws IllegalArgumentException {
        // Assure que les données requises sont fournies
        if (!areValidRequestRentalFields(rentalRequest.getName(), rentalRequest.getSurface(), rentalRequest.getPrice())) {
            throw new IllegalArgumentException("Toutes les informations requises ne sont pas renseignées. Merci de tout renseigner.");
        }

        UserEntity currentUser = userRepository.findById(Integer.valueOf(currentUserId))
                .orElseThrow(()->new UserDoesNotExistException("User does not exist."));

        final String image = "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg";

        // Création de l'objet RentalDto
        RentalDto rentalDto = RentalDto.builder()
                .name(rentalRequest.getName())
                .price(rentalRequest.getPrice())
                .picture(image)
                .surface(rentalRequest.getSurface())
                .description(rentalRequest.getDescription())
                .ownerId(currentUser.getId())
                .build();

        RentalEntity rentalEntity = RentalMapper.mapToEntity(rentalDto);

        // Enregistre le nouvel objet dans la base de données en utilisant la méthode du repository
        return rentalRepository.save(rentalEntity);
    }

    /**
     * Met à jour les détails d'un bien en location avec les informations fournies.
     *
     * @param rentalUpdateRequest Les nouvelles informations à mettre à jour pour le bien en location.
     * @param id                  L'identifiant du bien en location à mettre à jour.
     * @return L'entité du bien en location mise à jour et enregistrée dans la base de données.
     * @throws IllegalArgumentException Si toutes les informations requises ne sont pas fournies.
     * @throws RentalDoesNotExistException Si le bien en location avec l'identifiant spécifié n'existe pas dans la base de données.
     */
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