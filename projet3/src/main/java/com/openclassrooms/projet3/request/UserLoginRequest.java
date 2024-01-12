package com.openclassrooms.projet3.request;

public class UserLoginRequest {
    // --------------------------------------
    // Champs nécessaire pour une connexion
    // --------------------------------------
    private String login;
    private String password;

    // --------------------------------------
    // Constructeur avec paramètres pour initialiser les champs lors de la création d'une instance
    // --------------------------------------
    public UserLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // --------------------------------------
    // Getter et Setter
    // --------------------------------------
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
