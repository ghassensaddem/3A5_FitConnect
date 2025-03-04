package com.esprit.models;

import java.util.Objects;

public class Event {
    private int id;
    private String date;
    private float prixdupass;
    private String lieu;
    private String horaire;
    private String image;
    private double revenu;
    public Event(int id, String date, float prixdupass, String lieu, String horaire ,String image) {
        this.id = id;
        this.date = date;
        this.prixdupass = prixdupass;
        this.lieu = lieu;
        this.horaire = horaire;
        this.image=image;
    }

    public Event(String date, float prixdupass, String lieu, String horaire,String image) {
        this.date = date;
        this.prixdupass = prixdupass;
        this.lieu = lieu;
        this.horaire = horaire;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getPrixdupass() {
        return prixdupass;
    }

    public String getLieu() {
        return lieu;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setPrixdupass(float prixdupass) {
        this.prixdupass = prixdupass;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }
    public double getRevenu() {
        return revenu;
    }

    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", prixdupass=" + prixdupass +
                ", lieu='" + lieu + '\'' +
                ", horaire='" + horaire + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Float.compare(prixdupass, event.prixdupass) == 0 && Objects.equals(date, event.date) && Objects.equals(lieu, event.lieu) && Objects.equals(horaire, event.horaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, prixdupass, lieu, horaire);
    }


}
