package com.esprit.models;

public final class Admin extends User {

    String role;


    public Admin(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email, String image, String role) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email, image);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
