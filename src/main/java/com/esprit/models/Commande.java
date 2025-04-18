package com.esprit.models;

import java.time.LocalDateTime;

public class Commande {
    private int id;
    private int clientId;
    private int equipementId;
    private String etat;
    private String statutPaiement;
    private int quantite;
    private LocalDateTime dateCreation;
    private Equipement equipement; // Ajout de l'attribut Equipement

    // Constructeurs
    public Commande(int id, int clientId, int equipementId, String etat, String statutPaiement, int quantite, LocalDateTime dateCreation) {
        this.id = id;
        this.clientId = clientId;
        this.equipementId = equipementId;
        this.etat = etat;
        this.statutPaiement = statutPaiement;
        this.quantite = quantite;
        this.dateCreation = dateCreation;
    }

    public Commande(int id, int clientId, int equipementId, String etat, String statutPaiement, int quantite) {
        this(id, clientId, equipementId, etat, statutPaiement, quantite, LocalDateTime.now());
    }

    public Commande(int clientId, int equipementId, String etat, String statutPaiement, int quantite) {
        this(0, clientId, equipementId, etat, statutPaiement, quantite, LocalDateTime.now());
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getEquipementId() {
        return equipementId;
    }

    public void setEquipementId(int equipementId) {
        this.equipementId = equipementId;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", equipementId=" + equipementId +
                ", etat='" + etat + '\'' +
                ", statutPaiement='" + statutPaiement + '\'' +
                ", quantite=" + quantite +
                ", dateCreation=" + (dateCreation != null ? dateCreation : "Non définie") +
                ", equipement=" + equipement +
                '}';
    }
}