package com.esprit.models;

public class PlanningActivity {
    private int idActivity;
    private int idSalle;
    private int capacityMax;
    private int nombreInscription;

    public PlanningActivity(int idActivity, int idSalle, int capacityMax, int nombreInscription) {
        this.idActivity = idActivity;
        this.idSalle = idSalle;
        this.capacityMax = capacityMax;
        this.nombreInscription = nombreInscription;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getCapacityMax() {
        return capacityMax;
    }

    public void setCapacityMax(int capacityMax) {
        this.capacityMax = capacityMax;
    }

    public int getNombreInscription() {
        return nombreInscription;
    }

    public void setNombreInscription(int nombreInscription) {
        this.nombreInscription = nombreInscription;
    }

    @Override
    public String toString() {
        return "PlanningActivity{" +
                "idActivity=" + idActivity +
                ", idSalle=" + idSalle +
                ", capacityMax=" + capacityMax +
                ", nombreInscription=" + nombreInscription +
                '}';
    }
}
