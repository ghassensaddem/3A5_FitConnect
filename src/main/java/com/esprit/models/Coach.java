package com.esprit.models;

public final class Coach extends User {
    private String lieuEngagement;
    private String specialite;

    public Coach(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email, String lieuEngagement, String specialite) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email);
        this.lieuEngagement = lieuEngagement;
        this.specialite = specialite;
    }


    public String getLieuEngagement() {
        return lieuEngagement;
    }

    public String getSpecialite() {
        return specialite;
    }


    public void setLieuEngagement(String lieuEngagement) {
        this.lieuEngagement = lieuEngagement;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "lieuEngagement='" + lieuEngagement + '\'' +
                ", specialite='" + specialite + '\'' +
                '}';
    }
}
