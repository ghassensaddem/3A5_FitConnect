package com.esprit.models;

import java.time.LocalDate;  // Importation de LocalDate

public class Post {
    private int id;
    private String author;
    private String contenu;
    private int upvotes;
    private int downvotes;
    private String image;
    private LocalDate date;  // Modification du type en LocalDate
    private int client_id;

    // Constructeur pour recherche
    public Post(int id, String author, String contenu, int upvotes, int downvotes, String image, LocalDate date, int client_id) {
        this.id = id;
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.image = image;
        this.date = date;
        this.client_id = client_id;
    }

    // Constructeur pour ajouter
    public Post(String author, String contenu, int upvotes, int downvotes, String image, LocalDate date, int client_id) {
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.image = image;
        this.date = date;
        this.client_id = client_id;
    }

    // Constructeur pour modification et suppression
    public Post(int id, String author, String contenu, int upvotes, int downvotes, String image) {
        this.id = id;
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.image = image;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }

    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDate getDate() { return date; }  // Modification du getter
    public void setDate(LocalDate date) { this.date = date; }  // Modification du setter

    public int getClientId() { return client_id; }
    public void setClientId(int client_id) { this.client_id = client_id; }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", contenu='" + contenu + '\'' +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", image='" + image + '\'' +
                ", date=" + date +  // Affichage de la date comme LocalDate
                ", client_id=" + client_id +
                '}';
    }
}
