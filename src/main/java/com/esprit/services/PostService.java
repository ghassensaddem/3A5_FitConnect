package com.esprit.services;

import com.esprit.models.Post;
import com.esprit.models.Commentaire;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService implements iService<Post> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Post post) {
        // Ajout du champ client_id dans la requête d'insertion
        String req = "INSERT INTO post (author, contenu, upvotes, downvotes, image, date, client_id) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, post.getAuthor());
            pst.setString(2, post.getContenu());
            pst.setInt(3, post.getUpvotes());
            pst.setInt(4, post.getDownvotes());
            pst.setString(5, post.getImage());
            pst.setInt(6, post.getClientId()); // Ajout de client_id
            pst.executeUpdate();
            System.out.println("Post ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Post post) {
        // Mise à jour avec client_id
        String req = "UPDATE post SET author=?, contenu=?, upvotes=?, downvotes=?, image=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, post.getAuthor());
            pst.setString(2, post.getContenu());
            pst.setInt(3, post.getUpvotes());
            pst.setInt(4, post.getDownvotes());
            pst.setString(5, post.getImage());
            pst.setInt(6, post.getId());
            pst.executeUpdate();
            System.out.println("Post modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Post post) {
        String req = "DELETE FROM post WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, post.getId());
            pst.executeUpdate();
            System.out.println("Post supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Post> rechercher() {
        ArrayList<Post> posts = new ArrayList<>();
        String req = "SELECT * FROM post";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getString("image"),
                        rs.getString("date"), // Récupérer la date
                        rs.getInt("client_id") // Récupérer client_id
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    public List<Commentaire> afficherCommentairesParPost(int postId) {
        List<Commentaire> commentaires = new ArrayList<>();
        // Mise à jour de la requête pour inclure client_id
        String req = "SELECT c.id, c.author, c.contenu, c.upvotes, c.downvotes, c.post_id, c.date, c.client_id " +
                "FROM commentaire c " +
                "INNER JOIN post p ON c.post_id = p.id " +
                "WHERE p.id = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, postId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Commentaire commentaire = new Commentaire(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getInt("post_id"),
                        rs.getString("date"),
                        rs.getInt("client_id")
                ); // Point-virgule ajouté ici
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commentaires;
    }

}
