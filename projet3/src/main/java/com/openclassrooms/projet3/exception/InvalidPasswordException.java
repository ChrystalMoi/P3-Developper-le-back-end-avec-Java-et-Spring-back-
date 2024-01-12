package com.openclassrooms.projet3.exception;

public class InvalidPasswordException extends RuntimeException {
    // Constructeur avec un message d'erreur à afficher lors de l'exception
    public InvalidPasswordException(String message) {
        super(message);
    }
}
