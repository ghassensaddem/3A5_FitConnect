package com.esprit.services;

import com.esprit.models.typeactivite;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class typeactiviteService implements iService<typeactivite> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(typeactivite activiteEvent) {
        String req = "INSERT INTO typeactivite (title, description,idActivite) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getTitle());
            pst.setString(2, activiteEvent.getDescription());
            pst.setInt(3, activiteEvent.getIdActivite());
            pst.executeUpdate();
            System.out.println("type ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(typeactivite activiteEvent) {
        String req = "UPDATE typeactivite SET title=?, description=?, idActivite=? WHERE id=? ";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getTitle());
            pst.setString(2, activiteEvent.getDescription());
            pst.setInt(3, activiteEvent.getId());
            pst.setInt(4, activiteEvent.getIdActivite());
        pst.executeUpdate();
            System.out.println("type modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(typeactivite activiteEvent) {
        String req = "DELETE FROM typeactivite WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, activiteEvent.getId());
            pst.executeUpdate();
            System.out.println("type supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<typeactivite> rechercher() {
        ArrayList<typeactivite> activites = new ArrayList<>();
        String req = "SELECT * FROM typeactivite";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                activites.add(new typeactivite(rs.getInt("id"), rs.getString("title"), rs.getString("description"),rs.getInt("idActivite")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activites;
    }
}
