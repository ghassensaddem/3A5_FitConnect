package com.esprit.models;

public class CategorieActivity {
    private int idCategorie;
    private String nomCategorie;
    private String description;

    public CategorieActivity(int idCategorie, String nomCategorie) {
        this.idCategorie = idCategorie;
        this.nomCategorie = nomCategorie;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    @Override
    public String toString() {
        return "CategorieActivity{" +
                "idCategorie=" + idCategorie +
                ", nomCategorie='" + nomCategorie + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
