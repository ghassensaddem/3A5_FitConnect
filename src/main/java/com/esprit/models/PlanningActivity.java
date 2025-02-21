package com.esprit.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PlanningActivity {
    private final IntegerProperty idActivity = new SimpleIntegerProperty();
    private final IntegerProperty idSalle = new SimpleIntegerProperty();
    private final IntegerProperty capacityMax = new SimpleIntegerProperty();
    private final IntegerProperty nombreInscription = new SimpleIntegerProperty();

    // Constructeur avec les propriétés
    public PlanningActivity(int idActivity, int idSalle, int capacityMax, int nombreInscription) {
        this.idActivity.set(idActivity);
        this.idSalle.set(idSalle);
        this.capacityMax.set(capacityMax);
        this.nombreInscription.set(nombreInscription);
    }

    // Getter et Setter pour idActivity
    public int getIdActivity() {
        return idActivity.get();
    }

    public void setIdActivity(int idActivity) {
        this.idActivity.set(idActivity);
    }

    public IntegerProperty idActivityProperty() {
        return idActivity;
    }

    // Getter et Setter pour idSalle
    public int getIdSalle() {
        return idSalle.get();
    }

    public void setIdSalle(int idSalle) {
        this.idSalle.set(idSalle);
    }

    public IntegerProperty idSalleProperty() {
        return idSalle;
    }

    // Getter et Setter pour capacityMax
    public int getCapacityMax() {
        return capacityMax.get();
    }

    public void setCapacityMax(int capacityMax) {
        this.capacityMax.set(capacityMax);
    }

    public IntegerProperty capacityMaxProperty() {
        return capacityMax;
    }

    // Getter et Setter pour nombreInscription
    public int getNombreInscription() {
        return nombreInscription.get();
    }

    public void setNombreInscription(int nombreInscription) {
        this.nombreInscription.set(nombreInscription);
    }

    public IntegerProperty nombreInscriptionProperty() {
        return nombreInscription;
    }

    @Override
    public String toString() {
        return "PlanningActivity{" +
                "idActivity=" + idActivity.get() +
                ", idSalle=" + idSalle.get() +
                ", capacityMax=" + capacityMax.get() +
                ", nombreInscription=" + nombreInscription.get() +
                '}';
    }
}
