package com.esprit.models;

import com.esprit.services.CategorieService;
import com.esprit.services.CategorieServiceS;

public class activity {
    private int idActivity;
    private String nomActivity;
    private String iconActivity;
    private int idCategorie; // Stocke l'ID de la catégorie

    public activity(int idActivity, String nomActivity, String iconActivity, int idCategorie) {
        this.idActivity = idActivity;
        this.nomActivity = nomActivity;
        this.iconActivity = iconActivity;
        this.idCategorie = idCategorie;
    }
    public String getCategorieNom() {
        CategorieServiceS categorieService = new CategorieServiceS();
        CategorieActivity categorie = categorieService.getCategorieById(this.idCategorie);
        return categorie != null ? categorie.getNomCategorie() : "Catégorie inconnue";
    }


    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public String getNomActivity() {
        return nomActivity;
    }

    public void setNomActivity(String nomActivity) {
        this.nomActivity = nomActivity;
    }

    public String getIconActivity() {
        return iconActivity;
    }

    public void setIconActivity(String iconActivity) {
        this.iconActivity = iconActivity;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "idActivity=" + idActivity +
                ", nomActivity='" + nomActivity + '\'' +
                ", iconActivity='" + iconActivity + '\'' +
                ", idCategorie=" + idCategorie +
                '}';
    }
}
