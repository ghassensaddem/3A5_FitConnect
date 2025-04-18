package com.esprit.services;

import com.esprit.models.seance;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceService {

    private Connection connection = DataSource.getInstance().getConnection();

    public List<Integer> getAllSeanceIds() {
        List<Integer> seanceIds = new ArrayList<>();
        String query = "SELECT id FROM seance";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                seanceIds.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seanceIds;
    }

    public int enregistrer(seance seance) {
        String sql = "INSERT INTO seance (date, horaire, programme_id, idActivity) VALUES (?, ?, ?, ?)";
        int generatedId = -1; // Valeur par défaut en cas d'échec

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, java.sql.Date.valueOf(seance.getDate().toString()));
            stmt.setTime(2, Time.valueOf(seance.getHoraire()));
            stmt.setInt(3, seance.getProgramme_id());
            stmt.setInt(4, seance.getActivite_id());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    seance.setId(generatedId);
                }
                System.out.println("Séance ajoutée avec succès (ID: " + generatedId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement: " + e.getMessage());
        }
        return generatedId; // Correction : on retourne bien un int
    }



    public void ajouter(seance seance) {
        enregistrer(seance);
    }

    public boolean modifier(seance seance) {
        String req = "UPDATE seance SET date=?, horaire=?,  programme_id=?, idActivity=? WHERE id=?";
        try (
            PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)){
            pst.setDate(1, seance.getDate());
            pst.setTime(2, java.sql.Time.valueOf(seance.getHoraire()));
            pst.setInt(3, seance.getProgramme_id());
            pst.setInt(4, seance.getActivite_id());
            pst.setInt(5, seance.getId());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                seance.setId(rs.getInt(1)); // Mettre à jour l'ID de l'objet
            }
            System.out.println("Seance modifiée");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;}


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
                        rs.getInt("programme_id"),
                        rs.getInt("idActivity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances pour le programme " + programmeId + ": " + e.getMessage());
        }

        return seances;
    }
    public seance getSeanceById(int id) {
        String req = "SELECT * FROM seance WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new seance(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),  // SQL Date -> LocalDate
                        rs.getTime("horaire").toLocalTime(), // SQL Time -> LocalTime
                        rs.getInt("programme_id"),
                        rs.getInt("idActivity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Retourne null si aucune séance n'est trouvée
    }

}
