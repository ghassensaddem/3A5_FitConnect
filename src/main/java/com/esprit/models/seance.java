package com.esprit.models;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;




public class seance {

    private int id;
    private LocalDate date;
    private LocalTime horaire;
    private String lieu;
    private int programme_id;
    private int activite_id;

    // ✅ Constructeur avec ID
    public seance(int id, LocalDate date, LocalTime horaire, String lieu, int programme_id, int activite_id) {

        this.id = id;
        this.date = date;
        this.horaire = horaire;
        this.lieu = lieu;
        this.programme_id = programme_id;
        this.activite_id = activite_id;
    }

    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public seance(LocalDate date, LocalTime horaire, String lieu, int programme_id, int activite_id) {
        this.date = date;
        this.horaire = horaire;
        this.lieu = lieu;
        this.programme_id = programme_id;
        this.activite_id = activite_id;
    }

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ✅ Retourne java.sql.Dat
    // e en convertissant LocalDate
    public Date getDate() {
        return java.sql.Date.valueOf(date);  // Retourner directement LocalDate
    }

    public void setdate(LocalDate date) {
        this.date = date;
    }


    public Time getHoraire() {
        return java.sql.Time.valueOf(horaire);  // Retourner directement LocalTime
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
    public void setProgramme_id(int programme_id) {
        this.programme_id = programme_id;
    }

public int getActivite_id() {
        return activite_id;
}
public void setactivite_id(int activite_id) {
        this.activite_id = activite_id;
}

    @Override
    public String toString() {
        return "seance{" +
                "id=" + id +
                ", date=" + date +
                ", horaire=" + horaire +
                ", lieu='" + lieu + '\'' +
                ", programme_id=" + programme_id +
                ", activite_id=" + activite_id +
                '}';
    }
}
