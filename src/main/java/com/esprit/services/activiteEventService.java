package com.esprit.services;

import com.esprit.models.activiteevent;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class activiteEventService implements iService<activiteevent> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(activiteevent activiteEvent) {
        String req = "INSERT INTO activiteevent (horaire, nbrparticipant, idEvent,idTypeActivite) VALUES (?,?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getHoraire());
            pst.setInt(2, activiteEvent.getNbrparticipant());
            pst.setInt(3, activiteEvent.getIdEvent());
            pst.setInt(4, activiteEvent.getIdTypeActivite());
            pst.executeUpdate();
            System.out.println("Activité ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(activiteevent activiteEvent) {
        String req = "UPDATE activiteevent SET horaire=?, nbrparticipant=?, idEvent=?,idTypeActivite=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getHoraire());
            pst.setInt(2, activiteEvent.getNbrparticipant());
            pst.setInt(3, activiteEvent.getIdEvent());
            pst.setInt(4, activiteEvent.getIdTypeActivite());
            pst.setInt(5, activiteEvent.getId());
            pst.executeUpdate();
            System.out.println("Activité modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(activiteevent activiteEvent) {
        String req = "DELETE FROM activiteevent WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, activiteEvent.getId());
            pst.executeUpdate();
            System.out.println("Activité supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<activiteevent> rechercher() {
        ArrayList<activiteevent> activites = new ArrayList<>();
        String req = "SELECT * FROM activiteevent";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                activites.add(new activiteevent(rs.getInt("id"), rs.getString("horaire"), rs.getInt("nbrparticipant"), rs.getInt("idEvent"),rs.getInt("idTypeActivite")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activites;
    }


    // Récupérer les IDs depuis la base de données
    public ArrayList<Integer> getAvailableIds(String table, String column) {
        ArrayList<Integer> ids = new ArrayList<>();

        // Construire dynamiquement la requête SQL
        String query = "SELECT " + column + " FROM " + table;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ids.add(rs.getInt(column)); // Récupérer les IDs
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Autre erreur : " + e.getMessage());
            e.printStackTrace();
        }

        return ids;
    }

}


