package com.esprit.models;



public class Avis {

    private int id; // Identifiant unique de l'avis
    private String commentaire; // Contenu de l'avis
    private int note; // Note sur 10, par exemple
    private int seanceId;

    // Constructeur
    public Avis(int id, String commentaire, int note, int seanceId) {
        this.id = id;
        this.commentaire = commentaire;
        this.note = note;
        this.seanceId = seanceId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }

    public int getSeanceId() { return seanceId; }
    public void setSeanceId(int seanceId) { this.seanceId = seanceId; }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", commentaire='" + commentaire + '\'' +
                ", note=" + note +
                ", seanceId=" + seanceId +
                '}';
    }
}
