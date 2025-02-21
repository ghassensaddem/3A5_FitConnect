package com.esprit.models;

public class Programme {

    private int id;
    private double prix;
    private String description;
    private int coach_id;



    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public Programme(int id, double prix, String description, int coach_id) {
        this.id = id;

        this.prix = prix;
        this.description = description;
        this.coach_id = coach_id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getCoach_id() {
        return coach_id;
    }
    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +

                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", coach_id=" + coach_id +
                '}';
    }


}
