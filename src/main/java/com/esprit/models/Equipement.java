package com.esprit.models;

public class Equipement {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteStock;
    private int idCategorie;  // Référence à la catégorie par son ID

    // Constructeur avec ID
    public Equipement(int id, String nom, String description, double prix, int quantiteStock, int idCategorie) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.idCategorie = idCategorie;
    }

    // Constructeur sans ID (utilisé avant insertion en BDD)
    public Equipement(String nom, String description, double prix, int quantiteStock, int idCategorie) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.idCategorie = idCategorie;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", quantiteStock=" + quantiteStock +
                ", idCategorie=" + idCategorie +
                '}';
    }
}