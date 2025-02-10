package com.esprit.models;

public final class Admin extends User {


    public Admin(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
