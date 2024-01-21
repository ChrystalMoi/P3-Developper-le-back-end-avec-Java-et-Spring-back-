package com.openclassrooms.projet3.exception;

public class RentalDoesNotExistException extends RuntimeException {
    // Constructeur avec un message d'erreur à afficher lors de l'exception
    public RentalDoesNotExistException(String message) {
        super(message);
    }
}