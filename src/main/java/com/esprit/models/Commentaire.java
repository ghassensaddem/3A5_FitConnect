package com.esprit.models;

import java.time.LocalDate;  // Importation de LocalDate

public class Commentaire {
    private int id;
    private String author;
    private String contenu;
    private int upvotes;
    private int downvotes;
    private int post_id;
    private LocalDate date;  // Modification du type en LocalDate
    private int client_id;

    // Constructeur pour recherche
    public Commentaire(int id, String author, String contenu, int upvotes, int downvotes, int post_id, LocalDate date, int client_id) {
        this.id = id;
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.post_id = post_id;
        this.date = date;
        this.client_id = client_id;
    }

    // Constructeur pour ajouter
    public Commentaire(String author, String contenu, int upvotes, int downvotes, LocalDate date, int post_id, int client_id) {
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.date = date;
        this.post_id = post_id;
        this.client_id = client_id;
    }

    // Constructeur pour modification et suppression
    public Commentaire(int id, String author, String contenu, int upvotes, int downvotes) {
        this.id = id;
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    // Getters et setters pour chaque attribut
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getPostId() {
        return post_id;
    }

    public void setPostId(int post_id) {
        this.post_id = post_id;
    }

    public LocalDate getDate() {
        return date;  // Getter pour date modifié
    }

    public void setDate(LocalDate date) {
        this.date = date;  // Setter pour date modifié
    }

    public int getClientId() {
        return client_id;
    }

    public void setClientId(int client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", contenu='" + contenu + '\'' +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", post_id=" + post_id +
                ", date=" + date +  // Affichage de la date comme LocalDate
                ", client_id=" + client_id +
                '}';
    }
}
