package com.esprit.services;

import com.esprit.models.seance;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class SeanceService  {

    private Connection connection = DataSource.getInstance().getConnection();

    public void enregistrer(seance seance) {
        String sql = "INSERT INTO seance ( date, horaire, lieu, programme_id, idActivity) VALUES ( ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(seance.getDate().toLocalDate()));  // LocalDate -> java.sql.Date
            stmt.setTime(2, java.sql.Time.valueOf(seance.getHoraire().toString()));  // LocalTime -> java.sql.Time
            stmt.setString(3, seance.getLieu());
            stmt.setInt(4, seance.getProgramme_id());
            stmt.setInt(5, seance.getActivite_id());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Séance ajoutée avec succès");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement dans la base de données: " + e.getMessage());
        }
    }


    public void ajouter(seance seance) {
        String req = "INSERT INTO seance (date, horaire, lieu, programme_id, idActivity) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setDate(1, seance.getDate()); // seance.getDate() retourne déjà java.sql.Date
            pst.setTime(2, seance.getHoraire()); // seance.getHoraire() retourne déjà java.sql.Time
            pst.setString(3, seance.getLieu());
            pst.setInt(4, seance.getProgramme_id());
            pst.setInt(5, seance.getActivite_id());
            pst.executeUpdate();
            System.out.println("Séance ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la séance : " + e.getMessage());
        }
    }


    public boolean modifier(seance seance) {
        String req = "UPDATE seance SET date=?, horaire=?, lieu=?, programme_id=?, idActivity=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setDate(1, seance.getDate());
            pst.setTime(2, seance.getHoraire());
            pst.setString(3, seance.getLieu());
            pst.setInt(4, seance.getProgramme_id());
            pst.setInt(5, seance.getActivite_id());
            pst.setInt(6, seance.getId());
            int rowsUpdated = pst.executeUpdate();  // Récupère le nombre de lignes affectées
            if (rowsUpdated > 0) {
                System.out.println("Séance modifié avec succès");
                return true;  // Si des lignes ont été modifiées, on retourne true
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;  // Retourne false si aucune ligne n'a été modifiée
    }


    public boolean supprimer(int id) {
        String req = "DELETE FROM seance WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);  // Utiliser directement l'ID
            pst.executeUpdate();
            System.out.println("Séance supprimée");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Aucun programme trouvé avec cet ID.");
        }
        return false;
    }


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
                        rs.getInt("idActivity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances : " + e.getMessage());
        }

        return seances;
    }

    // Nouvelle méthode pour rechercher les séances par programme_id
    public ArrayList<seance> rechercherParProgramme(int programmeId) {
        ArrayList<seance> seances = new ArrayList<>();
        String req = "SELECT * FROM seance WHERE programme_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, programmeId); // Utiliser le programmeId pour filtrer les séances
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                seances.add(new seance(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(), // SQL Date -> LocalDate
                        rs.getTime("horaire").toLocalTime(), // SQL Time -> LocalTime
                        rs.getString("lieu"),
                        rs.getInt("programme_id"),
                        rs.getInt("idActivity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances pour le programme " + programmeId + ": " + e.getMessage());
        }

        return seances;
    }
}
