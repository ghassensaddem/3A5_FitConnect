package com.esprit.services;

import com.esprit.models.PlanningActivity;
import com.esprit.models.activity;
import com.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningService  implements iService<PlanningActivity> {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(PlanningActivity planning) {
        String req = "INSERT INTO planningactivity (idActivity, idSalle, capacityMax, nombreInscription) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, planning.getIdActivity());
            pst.setInt(2, planning.getIdSalle());
            pst.setInt(3, planning.getCapacityMax());
            pst.setInt(4, planning.getNombreInscription());
            pst.executeUpdate();
            System.out.println("Planification ajoutée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la planification: " + e.getMessage());
        }
    }


    public void modifier(PlanningActivity planning) {
        String req = "UPDATE planningActivity SET capacityMax = ?, nombreInscription = ? WHERE idActivity = ? AND idSalle = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, planning.getCapacityMax());
            pst.setInt(2, planning.getNombreInscription());
            pst.setInt(3, planning.getIdActivity());
            pst.setInt(4, planning.getIdSalle());
            pst.executeUpdate();
            System.out.println("Planification modifiée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la planification: " + e.getMessage());
        }
    }


    public void supprimer(PlanningActivity planning) {
        String req = "DELETE FROM planningactivity WHERE idActivity = ? AND idSalle = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, planning.getIdActivity());
            pst.setInt(2, planning.getIdSalle());
            pst.executeUpdate();
            System.out.println("Planification supprimée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la planification: " + e.getMessage());
        }
    }


    public ArrayList<PlanningActivity> rechercher() {
        ArrayList<PlanningActivity> planningList = new ArrayList<>();
        String req = "SELECT * FROM planningactivity";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                planningList.add(new PlanningActivity(rs.getInt("idActivity"),
                         rs.getInt("idSalle"),
                        rs.getInt("capacityMax"),
                        rs.getInt("nombreInscription")
                ));

            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des planifications: " + e.getMessage());
        }
        return planningList;
    }
}
