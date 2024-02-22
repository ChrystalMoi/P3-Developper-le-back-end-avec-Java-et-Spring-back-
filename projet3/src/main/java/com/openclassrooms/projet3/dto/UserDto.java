package com.openclassrooms.projet3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    // Variables
    private Integer id;
    private String email;
    private String name;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    // Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String infoMe() {
        return String.format("{\"id\":%s, \"name\":\"%s\", \"email\":\"%s\", \"created_at\":\"%s\", \"updated_at\":\"%s\"}",
                getId(), getName(), getEmail(), getCreatedAt(), getUpdatedAt());
    }
}
