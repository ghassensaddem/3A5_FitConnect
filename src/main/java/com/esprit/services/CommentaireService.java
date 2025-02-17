package com.esprit.services;

import com.esprit.models.Commentaire;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;  // Importation de LocalDate
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Commentaire commentaire) {
        // Modification pour ajouter CURRENT_TIMESTAMP en base de données
        String req = "INSERT INTO commentaire (author, contenu, upvotes, downvotes, post_id, client_id, date) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, commentaire.getAuthor());
            pst.setString(2, commentaire.getContenu());
            pst.setInt(3, commentaire.getUpvotes());
            pst.setInt(4, commentaire.getDownvotes());
            pst.setInt(5, commentaire.getPostId());
            pst.setInt(6, commentaire.getClientId());
            // Pas besoin d'ajouter explicitement la date, elle est gérée par CURRENT_TIMESTAMP
            pst.executeUpdate();
            System.out.println("Commentaire ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Commentaire commentaire) {
        String req = "UPDATE commentaire SET author=?, contenu=?, upvotes=?, downvotes=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, commentaire.getAuthor());
            pst.setString(2, commentaire.getContenu());
            pst.setInt(3, commentaire.getUpvotes());
            pst.setInt(4, commentaire.getDownvotes());
            pst.setInt(5, commentaire.getId());
            pst.executeUpdate();
            System.out.println("Commentaire modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Commentaire commentaire) {
        String req = "DELETE FROM commentaire WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commentaire.getId());
            pst.executeUpdate();
            System.out.println("Commentaire supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Commentaire> rechercher() {
        ArrayList<Commentaire> commentaires = new ArrayList<>();
        String req = "SELECT * FROM commentaire";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Conversion de la date String en LocalDate
                String dateStr = rs.getString("date");
                LocalDate date = LocalDate.parse(dateStr); // Conversion de la date String à LocalDate

                commentaires.add(new Commentaire(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getInt("post_id"),
                        date,  // Passer LocalDate au lieu de String
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commentaires;
    }
}
