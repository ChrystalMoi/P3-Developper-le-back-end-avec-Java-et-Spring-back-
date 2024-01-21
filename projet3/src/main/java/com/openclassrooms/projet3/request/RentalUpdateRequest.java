package com.openclassrooms.projet3.request;

public class RentalUpdateRequest {

    // --------------------------------------
    // Champs nécessaire pour l'update d'un bien
    // --------------------------------------
    private String name;
    private Double surface;
    private Double price;
    private String description;

    // --------------------------------------
    // Constructeur avec paramètres pour initialiser les champs lors de l'update d'une instance
    // --------------------------------------
    public RentalUpdateRequest(String name, Double surface, Double price, String description) {
        this.name = name;
        this.surface = surface;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
