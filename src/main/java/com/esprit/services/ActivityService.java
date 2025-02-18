package com.esprit.services;
import com.esprit.utils.DataSource;

import com.esprit.models.activity;
import com.esprit.services.iService;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ActivityService implements iService<activity>{
    private Connection connection = DataSource.getInstance().getConnection();
    public void ajouter(activity activity) {
        String req = "INSERT INTO activity  (nomActivity, IconActivity, categorieActivity) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activity.getNomActivity());
            pst.setString(2, activity.getIconActivity());
            pst.setString(3, activity.getCategorieActivity());
            pst.executeUpdate();
            System.out.println("Activité ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(activity activity) {
        String req = "UPDATE activity SET nomActivity = ?, IconActivity = ?, categorieActivity = ? WHERE idActivity = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activity.getNomActivity());
            pst.setString(2, activity.getIconActivity());
            pst.setString(3, activity.getCategorieActivity());
            pst.setInt(4, activity.getIdActivity());
            pst.executeUpdate();
            System.out.println("Activité modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
                        rs.getString("categorieActivity")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activities;
    }

}