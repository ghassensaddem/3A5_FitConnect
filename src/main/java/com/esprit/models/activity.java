package com.esprit.models;

public class activity {
    private int idActivity;
    private String nomActivity;
    private String IconActivity;
    private String categorieActivity;

    public activity(int idAcitivity, String nomActivity, String iconAcitivity, String categorieAcitivity) {
        this.idActivity = idAcitivity;
        this.nomActivity = nomActivity;
        IconActivity = iconAcitivity;
        this.categorieActivity = categorieAcitivity;
    }


    public String getNomActivity() {
        return nomActivity;
    }

    public void setNomActivity(String nomAcitivity) {
        this.nomActivity = nomAcitivity;
    }

    public String getIconActivity() {
        return IconActivity;
    }

    public void setIconActivity(String iconAcitivity) {
        IconActivity = iconAcitivity;
    }

    public String getCategorieActivity() {
        return categorieActivity;
    }

    public void setCategorieActivity(String categorieAcitivity) {
        this.categorieActivity = categorieAcitivity;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idAcitivity) {
        this.idActivity = idAcitivity;
    }

    @Override
    public String toString() {
        return "activity{" +
                "idActivity=" + idActivity +
                ", nomActivity='" + nomActivity + '\'' +
                ", IconActivity='" + IconActivity + '\'' +
                ", categorieActivity='" + categorieActivity + '\'' +
                '}';
    }

}
