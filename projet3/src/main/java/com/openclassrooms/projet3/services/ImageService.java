package com.openclassrooms.projet3.services;

import com.openclassrooms.projet3.exception.ImageNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {
    private static final String DOSSIER_IMAGE = "C:\\Users\\crycr\\OneDrive\\Documents\\OpenClassrooms\\Projet 3 - Back-end en Java et Spring\\images";

    // Sauvegarde l'image dans le répertoire local
    public String sauvegardeImageDansDossier(MultipartFile fichierImage) throws IOException {
        // Création d'un nom unique avec un identifiant unique généré aléatoirement
        String nomFichierUnique = UUID.randomUUID().toString() + "_" + fichierImage.getOriginalFilename();

        // Récupération du chemin du dossier de stockage
        Path cheminDossierTelechargement = Path.of(DOSSIER_IMAGE);
        Path cheminFichier = cheminDossierTelechargement.resolve(nomFichierUnique);

        // Si le dossier de téléchargement n'existe pas alors, on le crée
        if (!Files.exists(cheminDossierTelechargement)) {
            Files.createDirectories(cheminDossierTelechargement);
        }

        // On place/copie le fichier dans le dossier des images
        Files.copy(fichierImage.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);

        // Retourne le nom du fichier
        return nomFichierUnique;
    }

    // Pour récupérer une image
    public String getImage(String nomImage) throws IOException, ImageNotFoundException {
        // On récupère le chemin du dossier image
        Path cheminImage = Path.of(DOSSIER_IMAGE, nomImage);

        String concatImage = DOSSIER_IMAGE + nomImage;

        return concatImage;
    }

    // Supprimer une image
    public void supprimerImage(String nomImage) throws IOException, ImageNotFoundException {
        Path imagePath = Path.of(DOSSIER_IMAGE, nomImage);

        // Si le fichier recherché existe, on le supprime
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
        // Sinon, on lance une exception
        else {
            throw new ImageNotFoundException("Image non trouvée.");
        }
    }
}
