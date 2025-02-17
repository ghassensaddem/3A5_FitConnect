package com.esprit.services;

import com.esprit.models.seance;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceService implements iService<seance> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(seance seance) {
        String req = "INSERT INTO seance (date, horaire, lieu, programme_id, idActivity ) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setDate(1, Date.valueOf(seance.getDate())); // LocalDate -> SQL Date
            pst.setTime(2, Time.valueOf(seance.getHoraire())); // LocalTime -> SQL Time
            pst.setString(3, seance.getLieu());
            pst.setInt(4, seance.getProgramme_id());
            pst.setInt(5, seance.getActivite_id());
            pst.executeUpdate();
            System.out.println("Séance ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la séance : " + e.getMessage());
        }
    }

    @Override
    public void modifier(seance seance) {
        String req = "UPDATE seance SET date=?, horaire=?, lieu=?, programme_id=?, idActivity =? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setDate(1, Date.valueOf(seance.getDate()));
            pst.setTime(2, Time.valueOf(seance.getHoraire()));
            pst.setString(3, seance.getLieu());
            pst.setInt(4, seance.getProgramme_id());
            pst.setInt(5, seance.getActivite_id());
            pst.setInt(6, seance.getId());
            pst.executeUpdate();
            System.out.println("Séance modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la séance : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(seance seance) {
        String req = "DELETE FROM seance WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, seance.getId());
            pst.executeUpdate();
            System.out.println("Séance supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la séance : " + e.getMessage());
        }
    }

    @Override
    public ArrayList<seance> rechercher() {
        ArrayList<seance> seances = new ArrayList<>();
        String req = "SELECT * FROM seance";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                seances.add(new seance(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(), // SQL Date -> LocalDate
                        rs.getTime("horaire").toLocalTime(), // SQL Time -> LocalTime
                        rs.getString("lieu"),
                        rs.getInt("programme_id"),
                        rs.getInt("idActivity ")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances : " + e.getMessage());
        }

        return seances;
    }
}
