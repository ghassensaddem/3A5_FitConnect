package com.esprit.models;

import java.util.Objects;

public class activiteevent {
    private int id;
    private String horaire;
    private int nbrparticipant;
    private int idEvent;
    private int idTypeActivite ;
    public activiteevent(int id, String horaire, int nbrparticipant, int idEvent,int idTypeActivite ) {
        this.id = id;
        this.horaire = horaire;
        this.nbrparticipant = nbrparticipant;
        this.idEvent = idEvent;
        this.idTypeActivite = idTypeActivite;
    }
    public activiteevent( String horaire, int nbrparticipant, int idEvent ,int idTypeActivite ) {
        this.horaire = horaire;
        this.nbrparticipant = nbrparticipant;
        this.idEvent = idEvent;
        this.idTypeActivite = idTypeActivite;
    }

    public activiteevent() {
        this.horaire = horaire;
        this.nbrparticipant = nbrparticipant;
    }

    public int getIdTypeActivite() {
        return idTypeActivite;
    }

    public void setIdTypeActivite(int idTypeActivite) {
        this.idTypeActivite = idTypeActivite;
    }

    public int getId() {
        return id;
    }

    public int getIdEvent() {
        return idEvent;
    }
    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public int getNbrparticipant() {
        return nbrparticipant;
    }

    public void setNbrparticipant(int nbrparticipant) {
        this.nbrparticipant = nbrparticipant;
    }


    @Override
    public String toString() {
        return "activiteevent{" +
                "id=" + id +
                ", horaire='" + horaire + '\'' +
                ", nbrparticipant=" + nbrparticipant +
                ", idEvent=" + idEvent +
                ", idTypeActivite=" + idTypeActivite +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        activiteevent that = (activiteevent) o;
        return id == that.id && nbrparticipant == that.nbrparticipant && idEvent == that.idEvent && idTypeActivite == that.idTypeActivite && Objects.equals(horaire, that.horaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaire, nbrparticipant, idEvent, idTypeActivite);
    }
}
