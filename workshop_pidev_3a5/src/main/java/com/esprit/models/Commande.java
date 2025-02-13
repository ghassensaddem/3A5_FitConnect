package com.esprit.models;

import java.time.LocalDateTime;

public class Commande {
    private int id;
    private int clientId;
    private int equipementId;
    private String etat;
    private LocalDateTime dateLivraison;
    private String statutPaiement;
    private int quantite;
    private LocalDateTime dateCreation;

    // Constructeur avec tous les paramètres
    public Commande(int id, int clientId, int equipementId, String etat, LocalDateTime dateLivraison, String statutPaiement, int quantite, LocalDateTime dateCreation) {
        this.id = id;
        this.clientId = clientId;
        this.equipementId = equipementId;
        this.etat = etat;
        this.dateLivraison = dateLivraison;
        this.statutPaiement = statutPaiement;
        this.quantite = quantite;
        this.dateCreation = dateCreation;
    }

    // Constructeur avec ID et dateCreation automatique
    public Commande(int id, int clientId, int equipementId, String etat, LocalDateTime dateLivraison, String statutPaiement, int quantite) {
        this(id, clientId, equipementId, etat, dateLivraison, statutPaiement, quantite, LocalDateTime.now());
    }

    // Constructeur sans ID (utilisé avant insertion en BDD)
    public Commande(int clientId, int equipementId, String etat, LocalDateTime dateLivraison, String statutPaiement, int quantite) {
        this(0, clientId, equipementId, etat, dateLivraison, statutPaiement, quantite, LocalDateTime.now());
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

    public LocalDateTime getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", equipementId=" + equipementId +
                ", etat='" + etat + '\'' +
                ", dateLivraison=" + (dateLivraison != null ? dateLivraison : "Non définie") +
                ", statutPaiement='" + statutPaiement + '\'' +
                ", quantite=" + quantite +
                ", dateCreation=" + (dateCreation != null ? dateCreation : "Non définie") +
                '}';
    }
}