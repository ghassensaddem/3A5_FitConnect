package com.esprit.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class RatingActivity {
    private final IntegerProperty idActivity = new SimpleIntegerProperty(this, "idActivity");
    private final IntegerProperty idSalle = new SimpleIntegerProperty(this, "idSalle");
    public static final int ID_CLIENT_FIXE = 2; // ID client par défaut pour les tests
    private final int idClient;
    private int ratingStars;
    private String review;

    // ✅ Constructeur principal
    public RatingActivity(int idActivity, int idSalle, int idClient, int ratingStars, String review) {
        setIdActivity(idActivity);
        setIdSalle(idSalle);
        this.idClient = idClient; // Permet d'avoir un client dynamique
        this.ratingStars = ratingStars;
        this.review = review;
    }

    // ✅ Constructeur alternatif (avec ID client fixe par défaut)
    public RatingActivity(int idActivity, int idSalle, int ratingStars, String review) {
        this(idActivity, idSalle, ID_CLIENT_FIXE, ratingStars, review);
    }

    public int getIdActivity() {
        return idActivity.get();
    }

    public void setIdActivity(int idActivity) {
        this.idActivity.set(idActivity);
    }

    public IntegerProperty idActivityProperty() {
        return idActivity;
    }

    public int getIdSalle() {
        return idSalle.get();
    }

    public void setIdSalle(int idSalle) {
        this.idSalle.set(idSalle);
    }

    public IntegerProperty idSalleProperty() {
        return idSalle;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(int ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "RatingActivity{" +
                "idActivity=" + getIdActivity() +
                ", idSalle=" + getIdSalle() +
                ", idClient=" + getIdClient() +
                ", ratingStars=" + ratingStars +
                ", review='" + review + '\'' +
                '}';
    }
}
