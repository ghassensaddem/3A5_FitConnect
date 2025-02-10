package com.esprit.models;

public class RatingActivity {
    private int idActivity ;
    private int idSalle ;
    private int ratingStars;
    private String review;

    public RatingActivity(int idActivity, int idSalle, int ratingStars, String review) {
        this.idActivity = idActivity;
        this.idSalle = idSalle;
        this.ratingStars = ratingStars;
        this.review = review;
    }

    public int getIdActivity() {
        return idActivity;
    }
    public int getIdSalle() {
        return idSalle;
    }

    public void setIdActivity(int idAcitivity) {
        this.idActivity = idAcitivity;
    }
    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(int avis_etoile) {
        this.ratingStars = avis_etoile;
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
                "idActivity=" + idActivity +
                ", idSalle=" + idSalle +
                ", ratingStars=" + ratingStars +
                ", review='" + review + '\'' +
                '}';
    }
}
