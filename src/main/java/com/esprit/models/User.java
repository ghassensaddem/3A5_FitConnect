package com.esprit.models;

public abstract sealed class User permits Client,Coach,Admin{

    private int id;
    private String nom;
    private String prenom;
    private String sexe;
    private String mdp;
    private String DateNaissance;
    private String email;
    private String image;


    public User(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.mdp = mdp;
        DateNaissance = dateNaissance;
        this.email = email;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public String getMdp() {
        return mdp;
    }

    public String getDateNaissance() {
        return DateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setDateNaissance(String dateNaissance) {
        DateNaissance = dateNaissance;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "admin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", mdp='" + mdp + '\'' +
                ", DateNaissance='" + DateNaissance + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
