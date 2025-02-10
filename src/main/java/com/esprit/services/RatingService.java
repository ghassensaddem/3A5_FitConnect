package com.esprit.services;

import com.esprit.models.RatingActivity;
import com.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingService implements iService<RatingActivity> {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(RatingActivity rating) {
        String req = "INSERT INTO ratingactivity (idActivity , idSalle,ratingStars, review) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, rating.getIdActivity());
            pst.setInt(2, rating.getIdSalle());
            pst.setInt(3, rating.getRatingStars());
            pst.setString(4, rating.getReview());
            pst.executeUpdate();
            System.out.println("Évaluation ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void modifier(RatingActivity rating) {
        String req = "UPDATE ratingactivity SET ratingStars = ?, review = ? WHERE idSalle = ? AND idActivity = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, rating.getRatingStars());
            pst.setString(2, rating.getReview());
            pst.setInt(3, rating.getIdSalle());
            pst.setInt(4, rating.getIdActivity());
            pst.executeUpdate();
            System.out.println("Évaluation modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Supprimer une évaluation
    public void supprimer(RatingActivity rating) {
        String req = "DELETE FROM ratingactivity WHERE idActivity = ? and idSalle = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, rating.getIdActivity());
            pst.setInt(2, rating.getIdSalle());
            pst.executeUpdate();
            System.out.println("Évaluation supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Rechercher toutes les évaluations
    public ArrayList<RatingActivity> rechercher() {
        ArrayList<RatingActivity> ratings = new ArrayList<>();
        String req = "SELECT * FROM ratingactivity";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ratings.add(new RatingActivity(
                        rs.getInt("idActivity"),
                        rs.getInt("idSalle"),
                        rs.getInt("ratingStars"),
                        rs.getString("review")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ratings;
    }

}
