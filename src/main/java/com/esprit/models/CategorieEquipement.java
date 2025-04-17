package com.esprit.models;

public class CategorieEquipement {

    private int id;
    private String nom;
    private String description;

    public CategorieEquipement() {
    }

    public CategorieEquipement(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }
    public CategorieEquipement(int id, String nom) {
        this.id = id;
        this.nom = nom;

    }

    public CategorieEquipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }


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

    @Override
    public String toString() {
        return "CategorieEquipement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
