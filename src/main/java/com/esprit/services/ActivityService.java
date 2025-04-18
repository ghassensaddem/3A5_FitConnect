package com.esprit.services;

import com.esprit.models.activity;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import com.esprit.models.CategorieActivity;

public class ActivityService implements iService<activity> {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(activity activity) {
        String req = "INSERT INTO activity (nomActivity, IconActivity, categorieActivity) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activity.getNomActivity());
            pst.setString(2, activity.getIconActivity());
            pst.setInt(3, activity.getIdCategorie()); // Maintenant stocké comme un int
            pst.executeUpdate();
            System.out.println("Activité ajoutée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'activité : " + e.getMessage());
        }
    }

    public void modifier(activity activity) {
        String req = "UPDATE activity SET nomActivity = ?, IconActivity = ?, categorieActivity = ? WHERE idActivity = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activity.getNomActivity());
            pst.setString(2, activity.getIconActivity());
            pst.setInt(3, activity.getIdCategorie()); // Maintenant stocké comme un int
            pst.setInt(4, activity.getIdActivity());
            pst.executeUpdate();
            System.out.println("Activité modifiée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'activité : " + e.getMessage());
        }
    }

    public void supprimer(activity activity) {
        String req = "DELETE FROM activity WHERE idActivity = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, activity.getIdActivity());
            pst.executeUpdate();
            System.out.println("Activité supprimée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'activité : " + e.getMessage());
        }
    }

    public ArrayList<activity> rechercher() {
        ArrayList<activity> activities = new ArrayList<>();
        String req = "SELECT * FROM activity";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                activities.add(new activity(
                        rs.getInt("idActivity"),
                        rs.getString("nomActivity"),
                        rs.getString("IconActivity"),
                        rs.getInt("categorieActivity") // Maintenant récupéré comme un int
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des activités : " + e.getMessage());
        }
        return activities;
    }
    public CategorieActivity getCategorieById(int idCategorie) {
        String req = "SELECT * FROM categorie WHERE idCategorie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, idCategorie);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new CategorieActivity(rs.getInt("idCategorie"), rs.getString("nomCategorie"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Retourne null si la catégorie n'est pas trouvée
    }

    public ArrayList<activity> getActivitiesByCategorie(int idCategorie) {
        ArrayList<activity> activities = new ArrayList<>();
        String req = "SELECT * FROM activity WHERE categorieActivity = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, idCategorie);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                activities.add(new activity(
                        rs.getInt("idActivity"),
                        rs.getString("nomActivity"),
                        rs.getString("IconActivity"),
                        rs.getInt("categorieActivity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des activités par catégorie : " + e.getMessage());
        }
        return activities;
    }


}
