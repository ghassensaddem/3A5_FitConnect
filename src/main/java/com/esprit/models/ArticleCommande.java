package com.esprit.models;

import javafx.scene.image.ImageView;

public class ArticleCommande {
    private ImageView produit;
    private String description;
    private double prix;
    private int quantite;
    private double total;

    // Constructeur
    public ArticleCommande(ImageView produit, String description, double prix, int quantite, double total) {
        this.produit = produit;
        this.description = description;
        this.prix = prix;
        this.quantite = quantite;
        this.total = total;
    }

    // Getters et Setters
    public ImageView getProduit() {
        return produit;
    }

    public void setProduit(ImageView produit) {
        this.produit = produit;
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

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}