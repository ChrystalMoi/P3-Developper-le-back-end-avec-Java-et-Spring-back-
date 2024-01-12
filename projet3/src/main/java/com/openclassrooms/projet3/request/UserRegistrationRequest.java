package com.openclassrooms.projet3.request;

/**
 * Représente les données nécessaires pour une inscription utilisateur
 */
public class UserRegistrationRequest {

    // --------------------------------------
    // Champs nécessaire pour une inscription
    // --------------------------------------
    private String email;
    private String password;
    private String name;

    // --------------------------------------
    // Constructeur par défaut sans paramètres
    // --------------------------------------
    public UserRegistrationRequest() {}

    // --------------------------------------
    // Constructeur avec paramètres pour initialiser les champs lors de la création d'une instance
    // --------------------------------------
    public UserRegistrationRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // --------------------------------------
    // Getter et Setter
    // --------------------------------------
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
