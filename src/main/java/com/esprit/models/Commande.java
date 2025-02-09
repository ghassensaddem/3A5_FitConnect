package com.esprit.models;

import java.time.LocalDateTime;

public class Commande {
    private int id;
    private int clientId;
    private String etat;
    private LocalDateTime dateLivraison;
    private String statutPaiement;

    // Constructeur avec ID
    public Commande(int id, int clientId, String etat, LocalDateTime dateLivraison, String statutPaiement) {
        this.id = id;
        this.clientId = clientId;
        this.etat = etat;
        this.dateLivraison = dateLivraison;
        this.statutPaiement = statutPaiement;
    }

    // Constructeur sans ID (utilisé avant insertion en BDD)
    public Commande(int clientId, String etat, LocalDateTime dateLivraison, String statutPaiement) {
        this.clientId = clientId;
        this.etat = etat;
        this.dateLivraison = dateLivraison;
        this.statutPaiement = statutPaiement;
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

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", etat='" + etat + '\'' +
                ", dateLivraison=" + (dateLivraison != null ? dateLivraison : "Non définie") +
                ", statutPaiement='" + statutPaiement + '\'' +
                '}';
    }
}