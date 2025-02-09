package com.esprit.models;

public class Post {
    private int id;
    private String author;
    private String contenu;
    private int upvotes;
    private int downvotes;
    private String image;
    private String date;  // Ajout du champ date


    public Post(int id, String author, String contenu, int upvotes, int downvotes, String image, String date) {
        this.id = id;
        this.author = author;
        this.contenu = contenu;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.image = image;
        this.date = date;
    }


    public Post(String author, String contenu, int upvotes, int downvotes, String image) {
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
                '}';
    }
}
