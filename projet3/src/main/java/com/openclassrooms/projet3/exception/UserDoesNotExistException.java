package com.openclassrooms.projet3.exception;

public class UserDoesNotExistException extends RuntimeException {
    // Constructeur avec un message d'erreur à afficher lors de l'exception
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
