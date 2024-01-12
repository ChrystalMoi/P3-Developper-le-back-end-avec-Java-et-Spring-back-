package com.openclassrooms.projet3.exception;

/**
 * Exception personnalisée pour indiquer qu'un utilisateur existe déjà
 */
public class UserAlreadyExistsException extends RuntimeException {
    // Constructeur avec un message d'erreur à afficher lors de l'exception
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
