package com.openclassrooms.projet3.request;

public class UserLoginRequest {
    // --------------------------------------
    // Champs nécessaire pour une connexion
    // --------------------------------------
    private String email;
    private String password;

    // --------------------------------------
    // Constructeur avec paramètres pour initialiser les champs lors de la création d'une instance
    // --------------------------------------
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
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
}
