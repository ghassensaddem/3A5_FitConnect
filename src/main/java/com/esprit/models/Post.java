package com.esprit.models;

public class Post {
    private int id;
    private String author;
    private String contenu;
    private int upvotes;
    private int downvotes;
    private String image;
    private String date;  // Ajout du champ date
    private int client_id;  // Ajout du champ client_id


    // Constructeur pour recherche
    public Post(int id, String author, String contenu, int upvotes, int downvotes, String image, String date, int client_id) {
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
    public Post(String author, String contenu, int upvotes, int downvotes, String image, String date,int client_id) {
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.image = image;
        this.date = date;
        this.client_id = client_id;
    }


    //constructeur pour modification et suppression
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

    public String getDate() { return date; } // Getter pour la date
    public void setDate(String date) { this.date = date; } // Setter pour la date

    public int getClientId() { return client_id; } // Getter pour client_id
    public void setClientId(int client_id) { this.client_id = client_id; } // Setter pour client_id

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", contenu='" + contenu + '\'' +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", client_id=" + client_id +  // Ajout de client_id dans toString
                '}';
    }
}
