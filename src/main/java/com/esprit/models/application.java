package com.esprit.models;

import java.time.LocalDate;

public class application {

    private int id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int idProgramme;
    private int idClient;

    // ✅ Constructeur
    public application( int id,LocalDate dateDebut, LocalDate dateFin, int idProgramme, int idClient) {
        this.id=id;

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idProgramme = idProgramme;
        this.idClient = idClient;
    }
    // 🔹 Constructeur avec 4 paramètres (sans ID)
    public application(LocalDate dateDebut, LocalDate dateFin, int idProgramme, int idClient) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idProgramme = idProgramme;
        this.idClient = idClient;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", idProgramme=" + idProgramme +
                ", idClient=" + idClient +
                '}';
    }
}
