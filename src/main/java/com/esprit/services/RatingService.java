package com.esprit.services;

import com.esprit.models.RatingActivity;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingService implements iService<RatingActivity> {

    private Connection connection;

    public RatingService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    public List<RatingActivity> getCommentaires(int idActivity, int idSalle) {
        List<RatingActivity> commentaires = new ArrayList<>();
        String req = "SELECT * FROM ratingactivity WHERE idActivity = ? AND idSalle = ? ORDER BY idActivity DESC";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, idActivity);
            pst.setInt(2, idSalle);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                commentaires.add(new RatingActivity(
                        rs.getInt("idActivity"),
                        rs.getInt("idSalle"),
                        rs.getInt("idClient"),
                        rs.getInt("ratingStars"),
                        rs.getString("review")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires: " + e.getMessage());
        }
        return commentaires;
    }

    public void ajouter(RatingActivity ratingActivity) {
        String req = "INSERT INTO ratingactivity (idActivity, idSalle, idClient, ratingStars, review) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, ratingActivity.getIdActivity());
            pst.setInt(2, ratingActivity.getIdSalle());
            pst.setInt(3, ratingActivity.getIdClient()); // ID client fixe
            pst.setInt(4, ratingActivity.getRatingStars());
            pst.setString(5, ratingActivity.getReview());
            pst.executeUpdate();
            System.out.println("Évaluation d'activité ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'évaluation d'activité: " + e.getMessage());
        }
    }

    public boolean existsRatingActivity(int idActivity, int idSalle) {
        String query = "SELECT COUNT(*) FROM ratingactivity WHERE idActivity = ? AND idSalle = ? AND idClient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idActivity);
            stmt.setInt(2, idSalle);
            stmt.setInt(3, RatingActivity.ID_CLIENT_FIXE); // Vérifie avec l'ID client fixe
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'évaluation: " + e.getMessage());
        }
        return false;
    }

    public void modifier(RatingActivity ratingActivity) {
        String req = "UPDATE ratingactivity SET ratingStars = ?, review = ? WHERE idActivity = ? AND idSalle = ? AND idClient = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, ratingActivity.getRatingStars());
            pst.setString(2, ratingActivity.getReview());
            pst.setInt(3, ratingActivity.getIdActivity());
            pst.setInt(4, ratingActivity.getIdSalle());
            pst.setInt(5, ratingActivity.getIdClient()); // ID client fixe
            pst.executeUpdate();
            System.out.println("Évaluation d'activité modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'évaluation d'activité: " + e.getMessage());
        }
    }

    public void supprimer(RatingActivity ratingActivity) {
        String req = "DELETE FROM ratingactivity WHERE idActivity = ? AND idSalle = ? AND idClient = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, ratingActivity.getIdActivity());
            pst.setInt(2, ratingActivity.getIdSalle());
            pst.setInt(3, ratingActivity.getIdClient()); // ID client fixe
            pst.executeUpdate();
            System.out.println("Évaluation d'activité supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'évaluation d'activité: " + e.getMessage());
        }
    }

    public ArrayList<RatingActivity> rechercher() {
        ArrayList<RatingActivity> ratingActivityList = new ArrayList<>();
        String req = "SELECT * FROM ratingactivity";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ratingActivityList.add(new RatingActivity(
                        rs.getInt("idActivity"),
                        rs.getInt("idSalle"),
                        rs.getInt("idClient"),
                        rs.getInt("ratingStars"),
                        rs.getString("review")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des évaluations d'activités: " + e.getMessage());
        }
        return ratingActivityList;
    }
}
