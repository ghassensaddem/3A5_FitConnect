package com.esprit.services;

import com.esprit.models.Post;
import com.esprit.models.Commentaire;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;  // Importation de LocalDate
import java.util.ArrayList;
import java.util.List;

public class PostService implements IService<Post> {

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
        // Mettre à jour la date avec la date actuelle
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
                // Conversion String en LocalDate pour le champ date
                LocalDate date = rs.getDate("date").toLocalDate();
                posts.add(new Post(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getString("image"),
                        date,  // Date convertie en LocalDate
                        rs.getInt("client_id")
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
                // Conversion String en LocalDate pour le champ date
                LocalDate date = rs.getDate("date").toLocalDate();
                Commentaire commentaire = new Commentaire(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("contenu"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getInt("post_id"),
                        date, // Date convertie en LocalDate
                        rs.getInt("client_id")
                );
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commentaires;
    }

    public List<String> getAllClientEmails() {
        List<String> emails = new ArrayList<>();
        String req = "SELECT email FROM client"; // Requête pour récupérer les emails des clients
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("email")); // Récupère l'email et l'ajoute à la liste
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return emails;
    }

    public int getClientIdByEmail(String email) {
        int clientId = -1; // Valeur par défaut si l'email n'est pas trouvé
        String req = "SELECT id FROM client WHERE email = ?"; // Requête pour récupérer l'ID du client via son email
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email); // On met l'email dans la requête
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                clientId = rs.getInt("id"); // Si l'email est trouvé, on récupère l'ID
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clientId; // Retourne l'ID trouvé ou -1 si l'email n'existe pas
    }




}
