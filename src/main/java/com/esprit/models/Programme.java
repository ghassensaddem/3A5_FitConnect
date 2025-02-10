package com.esprit.models;

import java.time.LocalDate;
import java.time.ZoneId;

public class Programme {

    private int id;
    private LocalDate datedebut;
    private LocalDate datefin;
    private double prix;
    private String description;
    private int coach_id;



    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public Programme(int id, LocalDate datedebut, LocalDate datefin, double prix, String description, int coach_id) {
        this.id = id;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.prix = prix;
        this.description = description;
        this.coach_id = coach_id;
    }

    public Programme(int id, java.sql.Date datedebut, java.sql.Date datefin, double prix, String description, int coach_id) {
        this.id = id;
        this.datedebut = datedebut.toLocalDate(); // Conversion vers LocalDate
        this.datefin = datefin.toLocalDate();     // Conversion vers LocalDate
        this.prix = prix;
        this.description = description;
        this.coach_id = coach_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ✅ Retourne java.sql.Date en convertissant LocalDate
    public java.sql.Date getdatedebut() {
        return java.sql.Date.valueOf(datedebut);
    }

    public void setdatedebut(LocalDate datedebut) {
        this.datedebut = datedebut;
    }

    public java.sql.Date getdatefin() {
        return java.sql.Date.valueOf(datefin);
    }

    public void setDatefin(LocalDate datefin) {
        this.datefin = datefin;
    }

    public double getprix() {
        return prix;
    }

    public void setprix(double prix) {
        this.prix = prix;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }
    public int getCoach_id() {
        return coach_id;
    }
    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +
                ", datedebut=" + datedebut +
                ", datefin=" + datefin +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", coach_id=" + coach_id +
                '}';
    }
}
