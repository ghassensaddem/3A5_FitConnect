package com.esprit.models;

public class Commande_Equipement {
    private int id; // Identifiant unique de l'association
    private int commandeId; // Identifiant de la commande associée
    private int equipementId; // Identifiant de l'équipement commandé
    private int quantite; // Quantité de l'équipement dans la commande
    private double prixUnitaire; // Prix unitaire de l'équipement au moment de la commande

    // Constructeur avec ID
    public Commande_Equipement(int id, int commandeId, int equipementId, int quantite, double prixUnitaire) {
        this.id = id;
        this.commandeId = commandeId;
        this.equipementId = equipementId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // Constructeur sans ID (utilisé avant insertion en BDD)
    public Commande_Equipement(int commandeId, int equipementId, int quantite, double prixUnitaire) {
        this.commandeId = commandeId;
        this.equipementId = equipementId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getEquipementId() {
        return equipementId;
    }

    public void setEquipementId(int equipementId) {
        this.equipementId = equipementId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    // Méthode pour calculer le sous-total (quantité * prix unitaire)
    public double getSousTotal() {
        return quantite * prixUnitaire;
    }

    @Override
    public String toString() {
        return "Commande_Equipement{" +
                "id=" + id +
                ", commandeId=" + commandeId +
                ", equipementId=" + equipementId +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", sousTotal=" + getSousTotal() +
                '}';
    }
}