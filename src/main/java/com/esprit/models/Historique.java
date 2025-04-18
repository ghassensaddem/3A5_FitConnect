package com.esprit.models;

import java.sql.Timestamp;

public class Historique {
    private int id;
    private String action;
    private String entite;
    private Timestamp date;
    private String details;

    // Constructeurs
    public Historique() {}

    public Historique(int id, String action, String entite, Timestamp date, String details) {
        this.id = id;
        this.action = action;
        this.entite = entite;
        this.date = date;
        this.details = details;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Historique{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", entite='" + entite + '\'' +
                ", date=" + date +
                ", details='" + details + '\'' +
                '}';
    }
}
