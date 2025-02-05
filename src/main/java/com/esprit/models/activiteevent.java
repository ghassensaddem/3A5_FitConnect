package com.esprit.models;

import java.util.Objects;

public class activiteevent {
    private int id;
    private String horaire;
    private int nbrparticipant;
    private int idEvent;
    public activiteevent(int id, String horaire, int nbrparticipant, int idEvent) {
        this.id = id;
        this.horaire = horaire;
        this.nbrparticipant = nbrparticipant;
        this.idEvent = idEvent;
    }
    public activiteevent( String horaire, int nbrparticipant, int idEvent ) {
        this.horaire = horaire;
        this.nbrparticipant = nbrparticipant;
        this.idEvent = idEvent;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        activiteevent that = (activiteevent) o;
        return id == that.id &&
                nbrparticipant == that.nbrparticipant && Objects.equals(horaire, that.horaire) && idEvent == that.idEvent;

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaire, nbrparticipant, idEvent);
    }
}
