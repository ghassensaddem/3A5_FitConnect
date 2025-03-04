package com.esprit.models;

import java.util.Objects;

public class typeactivite {
    private int id;
    private String title;
    private String description;
    private int idActivite;
    public typeactivite(int id, String title, String description,int idActivite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.idActivite = idActivite;

    }
    public typeactivite( String title, String description,int idActivite) {

        this.title = title;
        this.description = description;
        this.idActivite = idActivite;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
public int getIdActivite() {
        return idActivite;
}
public void setIdActivite(int idActivite) {
        this.idActivite = idActivite;
}
    @Override
    public String toString() {
        return "typeactivite{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idActivite=" + idActivite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        typeactivite that = (typeactivite) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(description, that.description) && idActivite == that.idActivite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, idActivite);
    }
}
