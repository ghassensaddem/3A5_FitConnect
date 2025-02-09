package com.esprit.models;

import java.time.LocalDate;
import java.time.ZoneId;

import java.time.LocalTime;


public class seance {

    private int id;
    private LocalDate date;
    private LocalTime horaire;
    private String lieu;
    private int programme_id;

    // ✅ Constructeur avec ID
    public seance(int id, LocalDate date, LocalTime horaire, String lieu, int programme_id) {

        this.id = id;
        this.date = date;
        this.horaire = horaire;
        this.lieu = lieu;
        this.programme_id = programme_id;
    }

    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public seance(LocalDate date, LocalTime horaire, String lieu, int programme_id) {
        this.date = date;
        this.horaire = horaire;
        this.lieu = lieu;
        this.programme_id = programme_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ✅ Retourne java.sql.Date en convertissant LocalDate
    public LocalDate getDate() {
        return java.sql.Date.valueOf(date).toLocalDate();
    }

    public void setdate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getHoraire() {
        return horaire;
    }

    public void  sethoraire(LocalTime horaire) {
        this.horaire = horaire;
    }


    public String getLieu() {

        return lieu;
    }

    public void setlieu(String lieu) {
        this.lieu = lieu;
    }

    public int getProgramme_id() {
        return programme_id;
    }
    public void setprogramme_id(int programme_id) {
        this.programme_id = programme_id;
    }

    @Override
    public String toString() {
        return "seance{" +
                "id=" + id +
                ", date=" + date +
                ", horaire=" + horaire +
                ", lieu='" + lieu + '\'' +
                ", programme_id=" + programme_id +
                '}';
    }
}
