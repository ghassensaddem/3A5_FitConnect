package com.esprit.services;

import com.esprit.utils.DataSource;
import com.esprit.models.personne;
import com.esprit.services.iService;

import java.sql.*;
import java.util.ArrayList;

public class PersonneService implements iService<personne> {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(personne p) {
        String req = "INSERT INTO personne (nom, prenom) VALUES (?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.executeUpdate();
            System.out.println("Personne ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(personne p) {
        String req = "UPDATE personne SET nom = ?, prenom = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setInt(3, p.getId());
            pst.executeUpdate();
            System.out.println("Personne modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void supprimer(personne p) {
        String req = "DELETE FROM personne WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, p.getId());
            pst.executeUpdate();
            System.out.println("Personne supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<personne> rechercher() {
        ArrayList<personne> personnes = new ArrayList<>();
        String req = "SELECT * FROM personne";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                personnes.add(new personne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return personnes;
    }
}
