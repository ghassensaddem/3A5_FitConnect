package com.esprit.models;

public final class Client extends User {
    private float poids;
    private float taille;
    private int idEvent;
    private int id_prog ;

    public Client(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email,String image ,float poids, float taille,int idEvent,int id_prog) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email,image);
        this.poids = poids;
        this.taille = taille;
        this.idEvent = idEvent;
        this.id_prog = id_prog;
    }

    public Client(int id, String nom, String prenom, String sexe, String mdp, String dateNaissance, String email,String image, float poids, float taille) {
        super(id, nom, prenom, sexe, mdp, dateNaissance, email,image);
        this.poids = poids;
        this.taille = taille;
    }

    public int getId_prog() {
        return id_prog;
    }

    public void setId_prog(int id_prog) {
        this.id_prog = id_prog;
    }

    public int getIdEvent() {
        return idEvent;
    }
    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }


    public float getPoids() {
        return poids;
    }

    public float getTaille() {
        return taille;
    }


    @Override
    public String toString() {
        return "Client{" +
                "nom=" + getNom() +
                "prenom=" + getPrenom() +
                "sexe=" + getSexe() +
                "mdp=" + getMdp() +
                "dateNaissance=" + getDateNaissance() +
                "email=" + getEmail() +
                ", taille=" + taille +
                ", poids=" + poids +
                "id_prog=" + id_prog +
                ", idEvent=" + idEvent +
                '}';
    }
}