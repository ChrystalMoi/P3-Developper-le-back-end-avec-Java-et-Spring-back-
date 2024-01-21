package com.openclassrooms.projet3.request;

public class RentalCreationRequest {
    // --------------------------------------
    // Champs nécessaire pour l'ajout d'un bien
    // --------------------------------------
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;

    // --------------------------------------
    // Constructeur avec paramètres pour initialiser les champs lors de la création d'une instance
    // --------------------------------------
    public RentalCreationRequest(String name, Double surface, Double price, String picture, String description) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
    }

    // --------------------------------------
    // Getter et Setter
    // --------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
