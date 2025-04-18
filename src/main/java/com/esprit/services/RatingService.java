package com.esprit.services;

import com.esprit.models.RatingActivity;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class RatingService implements iService<RatingActivity> {

    private Connection connection;

    public RatingService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    public Map<Integer, String> recupererNomsSalles() {
        Map<Integer, String> nomsSalles = new HashMap<>();
        String query = "SELECT idSalle, nomSalle FROM sallesportif";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                nomsSalles.put(rs.getInt("idSalle"), rs.getString("nomSalle"));
            }
            System.out.println("Récupération des noms des salles réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des noms des salles : " + e.getMessage());
        }

        return nomsSalles;
    }

    public String getClientPhoneNumber(int idClient) {
        String query = "SELECT numeroTelephone FROM personne WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idClient);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("numeroTelephone");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du numéro de téléphone : " + e.getMessage());
        }
        return null; // Retourne null si le numéro n'est pas trouvé
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
            pst.setInt(3, ratingActivity.getIdClient());
            pst.setInt(4, ratingActivity.getRatingStars());
            pst.setString(5, ratingActivity.getReview());
            pst.executeUpdate();
            System.out.println("Évaluation d'activité ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'évaluation d'activité: " + e.getMessage());
        }
    }

    public Map<Integer, Double> calculerMoyenneRatingsParSalle() {
        Map<Integer, Double> moyenneRatings = new HashMap<>();
        String req = "SELECT idSalle, AVG(ratingStars) AS moyenne FROM ratingactivity GROUP BY idSalle";

        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                moyenneRatings.put(rs.getInt("idSalle"), rs.getDouble("moyenne"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la moyenne des évaluations : " + e.getMessage());
        }

        return moyenneRatings;
    }

    public void modifier(RatingActivity ratingActivity) {
        String req = "UPDATE ratingactivity SET ratingStars = ?, review = ? WHERE idActivity = ? AND idSalle = ? AND idClient = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, ratingActivity.getRatingStars());
            pst.setString(2, ratingActivity.getReview());
            pst.setInt(3, ratingActivity.getIdActivity());
            pst.setInt(4, ratingActivity.getIdSalle());
            pst.setInt(5, ratingActivity.getIdClient());
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
            pst.setInt(3, ratingActivity.getIdClient());
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

    public String getActivityNameById(int idActivity) {
        String query = "SELECT nomActivity FROM activity WHERE idActivity = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idActivity);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nomActivity");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de l'activité: " + e.getMessage());
        }
        return "Activité inconnue";
    }
    public String getActivityImageById(int idActivity) {
        String query = "SELECT IconActivity FROM activity WHERE idActivity = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idActivity);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("IconActivity");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'image de l'activité: " + e.getMessage());
        }
        return "default.png"; // Image par défaut si aucune image n'est trouvée
    }


    public String getSalleNameById(int idSalle) {
        String query = "SELECT nomSalle FROM sallesportif WHERE idSalle = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idSalle);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nomSalle");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de la salle: " + e.getMessage());
        }
        return "Salle inconnue";
    }

    public String getUserNameById(int idClient) {
        String query = "SELECT nom FROM personne WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idClient);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de l'utilisateur: " + e.getMessage());
        }
        return "Utilisateur inconnu";
    }

}
