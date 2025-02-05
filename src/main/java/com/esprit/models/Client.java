package com.esprit.models;

public final class Client extends User {
    private float poids;
    private float taille;
    private String objectif;

    public Client(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email, float poids, float taille, String objectif) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email);
        this.poids = poids;
        this.taille = taille;
        this.objectif = objectif;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public float getPoids() {
        return poids;
    }

    public float getTaille() {
        return taille;
    }

    public String getObjectif() {
        return objectif;
    }

    @Override
    public String toString() {
        return "Client{" +
                "poids=" + poids +
                ", taille=" + taille +
                ", objectif='" + objectif + '\'' +
                '}';
    }
}
