package com.esprit.models;

import java.util.Objects;

public class Event {
    private int id;
    private String date;
    private float prixdupass;
    private String lieu;
    private String horaire;
    public Event(int id, String date, float prixdupass, String lieu, String horaire) {
        this.id = id;
        this.date = date;
        this.prixdupass = prixdupass;
        this.lieu = lieu;
        this.horaire = horaire;
    }

    public Event(String date, float prixdupass, String lieu, String horaire) {
        this.date = date;
        this.prixdupass = prixdupass;
        this.lieu = lieu;
        this.horaire = horaire;
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
