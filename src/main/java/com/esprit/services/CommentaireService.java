package com.esprit.services;

import com.esprit.models.Commentaire;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Commentaire commentaire) {

        String req = "INSERT INTO commentaire (author, contenu, upvotes, downvotes, post_id, date) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, commentaire.getAuthor());
            pst.setString(2, commentaire.getContenu());
            pst.setInt(3, commentaire.getUpvotes());
            pst.setInt(4, commentaire.getDownvotes());
            pst.setInt(5, commentaire.getPostId());
            pst.executeUpdate();
            System.out.println("Commentaire ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Commentaire commentaire) {

        String req = "UPDATE commentaire SET author=?, contenu=?, upvotes=?, downvotes=?, post_id=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, commentaire.getAuthor());
            pst.setString(2, commentaire.getContenu());
            pst.setInt(3, commentaire.getUpvotes());
            pst.setInt(4, commentaire.getDownvotes());
            pst.setInt(5, commentaire.getPostId());
            pst.setInt(6, commentaire.getId());
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
    public List<Commentaire> rechercher() {
        List<Commentaire> commentaires = new ArrayList<>();
        String req = "SELECT * FROM commentaire";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                commentaires.add(new Commentaire(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getInt("post_id"),
                        rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commentaires;
    }
}
