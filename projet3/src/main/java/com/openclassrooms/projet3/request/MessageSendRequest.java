package com.openclassrooms.projet3.request;

public class MessageSendRequest {
    // --------------------------------------
    // Champs nécessaire pour l'ajout d'un bien
    // --------------------------------------
    private Integer rentalId;
    private Integer userId;
    private String message;

    // --------------------------------------
    // Controller
    // --------------------------------------
    public MessageSendRequest(Integer rental_id, Integer user_id, String message) {
        this.rentalId = rental_id;
        this.userId = user_id;
        this.message = message;
    }

    // --------------------------------------
    // Getter & Setter
    // --------------------------------------
    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rental_id) {
        this.rentalId = rental_id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
