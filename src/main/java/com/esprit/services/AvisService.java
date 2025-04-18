package com.esprit.services;

import com.esprit.models.Avis;
import com.esprit.utils.DataSource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvisService {

    private Connection connection = DataSource.getInstance().getConnection();

    public Avis getById(int avisId) {
        // Ceci est un exemple simple, tu devras l'adapter à ta logique de base de données.
        // Si tu utilises un ORM (comme Hibernate ou JPA), tu utiliseras une requête ou une méthode pour récupérer l'objet.

        // Exemple avec une liste fictive d'avis
        for (Avis avis : getAllAvis()) {
            if (avis.getId() == avisId) {
                return avis;
            }
        }

        return null; // Si l'avis n'est pas trouvé
    }

    // Méthode fictive pour obtenir tous les avis
    public List<Avis> getAllAvis() {
        // Retourne une liste fictive d'avis
        return new ArrayList<>(); // Remplace ceci par ta logique pour récupérer les avis de la base de données
    }

    public void enregistrer(Avis avis) {
        String sql = "INSERT INTO avis ( id,commentaire, note,  seanceID) VALUES ( ?, ?, ?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, avis.getId());
            stmt.setString(2, avis.getCommentaire());
            stmt.setInt(3, avis.getNote());
            stmt.setInt(4, avis.getSeanceId());


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Avis ajoutée avec succès");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement dans la base de données: " + e.getMessage());
        }
    }


    public void ajouter(Avis avis) {
        String req = "INSERT INTO avis ( id,commentaire, note, seanceId) VALUES (?, ?, ?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, avis.getId());
            pst.setString(2, avis.getCommentaire());
            pst.setInt(3, avis.getNote());
            pst.setInt(4, avis.getSeanceId());
            pst.executeUpdate();
            System.out.println("Avis ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public boolean modifier(Avis avis) {
        String req = "UPDATE avis SET commentaire=?, note=?, seanceId=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, avis.getCommentaire());
            pst.setInt(2, avis.getNote());
            pst.setInt(3, avis.getSeanceId());
            pst.setInt(4, avis.getId());

            pst.executeUpdate();
            System.out.println("Avis modifiée");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID fourni est invalide.");
            return false;
        }

        String req = "DELETE FROM avis WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Avis supprimé avec succès.");
                return true;
            } else {
                System.out.println("⚠️ Aucun avis trouvé avec cet ID : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'avis : " + e.getMessage());
        }
        return false;
    }





    public ArrayList<Avis> rechercher() {
        ArrayList<Avis> avisList = new ArrayList<>();

        String req = "SELECT * FROM avis";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                avisList.add(new Avis(
                        rs.getInt("id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getInt("seanceId")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return avisList;
    }

    public List<Avis> rechercherAvisParSeance(int seanceId) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE seanceid = ?";

        try {
            if (connection != null && !connection.isClosed()) {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, seanceId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Avis avis = new Avis();
                    avis.setId(rs.getInt("id"));
                    avis.setCommentaire(rs.getString("commentaire"));
                    avis.setNote(rs.getInt("note"));
                    avis.setSeanceId(rs.getInt("seanceId"));
                    avisList.add(avis);
                }
            } else {
                System.out.println("La connexion à la base de données est fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return avisList;
    }
    public List<Avis> getAvisForSeance(int seanceId) {
        List<Avis> avisList = new ArrayList<>();
        String query = "SELECT * FROM avis WHERE seanceId = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, seanceId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getInt("seanceId")
                );
                avisList.add(avis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }

}