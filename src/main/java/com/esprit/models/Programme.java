package com.esprit.models;

public class Programme {

    private int id;
    private double prix;
    private String description;
    private int coach_id;
    private String lieu;

    public Programme( int id,double prix, String description, int coach_id,String lieu) {

        this.id=id;
        this.prix = prix;
        this.description = description;
        this.coach_id = coach_id;
        this.lieu=lieu;
    }
    // ✅ Constructeur qui accepte java.util.Date et les convertit en LocalDate
    public Programme( double prix, String description, int coach_id,String lieu) {


        this.prix = prix;
        this.description = description;
        this.coach_id = coach_id;
        this.lieu=lieu;
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
    public String getLieu(){return lieu;}

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +

                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", coach_id=" + coach_id +
                ", lieu=" + lieu +
                '}';
    }


}
