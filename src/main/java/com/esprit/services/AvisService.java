package com.esprit.services;

import com.esprit.models.Avis;
import com.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AvisService {

    private Connection connection = DataSource.getInstance().getConnection();
    public void enregistrer(Avis avis) {
        String sql = "INSERT INTO avis ( commentaire, note,  seanceID) VALUES ( ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, avis.getCommentaire());
            stmt.setInt(2, avis.getNote());
            stmt.setInt(3, avis.getSeanceId());


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
        String req = "INSERT INTO avis ( commentaire, note, seanceId) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, avis.getCommentaire());
            pst.setInt(2, avis.getNote());
            pst.setInt(3, avis.getSeanceId());
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

            int rowsUpdated = pst.executeUpdate();  // Récupère le nombre de lignes affectées
            if (rowsUpdated > 0) {
                System.out.println("Avis modifié avec succès");
                return true;  // Si des lignes ont été modifiées, on retourne true
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;  // Retourne false si aucune ligne n'a été modifiée
    }



    public boolean supprimer(int id) {
        String req = "DELETE FROM avis WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);  // Utiliser directement l'ID
            pst.executeUpdate();
            System.out.println("Avis supprimé");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Aucun programme trouvé avec cet ID.");
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
}
