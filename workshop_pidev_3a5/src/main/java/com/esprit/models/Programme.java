package com.esprit.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Programme {

    private int id;
    private LocalDate datedebut;
    private LocalDate datefin;
    private double prix;
    private String description;

    // ✅ Constructeur avec ID
    public Programme(int id, LocalDate datedebut, LocalDate datefin, double prix, String description) {
        this.id = id;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.prix = prix;
        this.description = description;
    }

    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public Programme(Date datedebut, Date datefin, double prix, String description) {
        this.datedebut = datedebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.datefin = datefin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.prix = prix;
        this.description = description;
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

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +
                ", datedebut=" + datedebut +
                ", datefin=" + datefin +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                '}';
    }
}
