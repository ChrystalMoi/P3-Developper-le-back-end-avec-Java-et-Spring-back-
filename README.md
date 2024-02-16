# Projet 3 : Développer le back-end avec Java et Spring - Chrystal BASTID

---

## Table des matières

1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Installation](#installation)
    - [Cloner le projet](#cloner-le-projet)
    - [Configurer la base de données](#configurer-la-base-de-données)
    - [Lancer l'application](#lancer-lapplication)
4. [Utilisation](#utilisation)
    - [Accéder à l'application](#accéder-à-lapplication)
    - [Endpoints disponibles](#endpoints-disponibles)
    - [Swagger](#swagger)

---

## Introduction

Ce projet est une application back-end développée en Java avec le framework Spring. 
Il s'agit du projet 3 de la formation "Développeur Full-Stack Java & Angular" proposer par OpenClassrooms.

---

## Prérequis

Avant de commencer, assurez-vous que votre environnement de développement répond aux prérequis suivants :

- Java JDK 17
- Maven
- Base de données MySQL

---

## Installation

### Cloner le projet

Utilisez la commande suivante pour cloner le projet depuis GitHub :

```bash
git clone git@github.com:ChrystalMoi/P3-Developper-le-back-end-avec-Java-et-Spring-back-.git
```

### Configurer la base de données

1. Création de la base de données : Exécutez le script SQL (```schema.sql```) fourni dans le dossier ```resources``` pour créer la base de données.

2. Configurer les propriétés de la base de données : 
Modifiez le fichier ```application.properties``` dans le dossier
   ```src/main/resources``` avec les paramètres appropriés pour votre base de données.

```
# Exemple de configuration pour MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/nom_de_votre_base
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
```

### Lancer l'application

Utilisez la commande Maven pour télécharger les dépendances du projet : ```mvn install```
Puis lancer l'application depuis votre ide (intellij).

---

## Utilisation
### Accéder à l'application

Une fois l'application lancée, vous pouvez accéder à l'API à l'adresse suivante : ```http://localhost:8080```

### Endpoints disponibles

#### Authentification et Utilisateurs

- Inscription : `/api/auth/register` (POST) - Endpoint pour gérer les requêtes d'inscription.

- Connexion : `/api/auth/login` (POST) - Endpoint pour gérer les requêtes de connexion.

- Profil utilisateur : `/api/auth/me` (GET) - Endpoint pour récupérer les informations du profil utilisateur.

#### Messages

- Envoyer un message : `/api/messages` (POST) - Endpoint pour envoyer des messages.

#### Locations

- Liste des locations : `/api/rentals` (GET) - Endpoint pour obtenir la liste des locations.

- Détails d'une location : `/api/rentals/:id` (GET) - Endpoint pour obtenir les détails d'une location.

- Mettre à jour une location : `/api/rentals/:id` (PUT) - Endpoint pour mettre à jour une location.

- Créer une location : `/api/rentals` (POST) - Endpoint pour créer une nouvelle location.

#### Utilisateurs

- Détails d'un utilisateur : `/api/user/:id` (GET) - Endpoint pour obtenir les détails d'un utilisateur.

### Swagger

Le swagger est visionable une fois que le projet est lancé sur le port 8080.
Lien du swagger : ```http://localhost:8080/swagger-ui.html```
