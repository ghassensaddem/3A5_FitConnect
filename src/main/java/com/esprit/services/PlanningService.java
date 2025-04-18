package com.esprit.services;

import com.esprit.models.PlanningActivity;
import com.esprit.models.SalleSportif;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningService implements iService<PlanningActivity> {

    // Utilisation de la méthode statique getConnection() de DataSource
    private Connection connection;

    public PlanningService() {
        // Initialisation de la connexion via DataSource
        this.connection = DataSource.getInstance().getConnection();
    }
    // Méthode pour récupérer la planification associée à une activité et une salle spécifique
    public PlanningActivity getPlanningByActivityAndSalle(int idActivity, int idSalle) {
        String query = "SELECT * FROM planningactivity WHERE idActivity = ? AND idSalle = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idActivity);
            pst.setInt(2, idSalle);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new PlanningActivity(
                        rs.getInt("idActivity"),
                        rs.getInt("idSalle"),
                        rs.getInt("capacityMax"),
                        rs.getInt("nombreInscription")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la planification: " + e.getMessage());
        }
        return null; // Retourne null si aucune planification n'est trouvée
    }

    // Méthode pour ajouter une planification
    public void ajouter(PlanningActivity planning) {
        String req = "INSERT INTO planningactivity (idActivity, idSalle, capacityMax, nombreInscription) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
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

    // Méthode pour vérifier si une planification existe déjà
    public boolean existsPlanning(int idActivity, int idSalle) {
        String query = "SELECT COUNT(*) FROM planningactivity WHERE idActivity = ? AND idSalle = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idActivity);
            stmt.setInt(2, idSalle);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si la combinaison existe, retourner true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Sinon, retourner false
    }

    // Méthode pour modifier une planification
    public void modifier(PlanningActivity planning) {
        String req = "UPDATE planningactivity SET capacityMax = ?, nombreInscription = ? WHERE idActivity = ? AND idSalle = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
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

    // Méthode pour supprimer une planification
    public void supprimer(PlanningActivity planning) {
        String req = "DELETE FROM planningactivity WHERE idActivity = ? AND idSalle = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, planning.getIdActivity());
            pst.setInt(2, planning.getIdSalle());
            pst.executeUpdate();
            System.out.println("Planification supprimée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la planification: " + e.getMessage());
        }
    }

    // Méthode pour récupérer toutes les planifications
    public ArrayList<PlanningActivity> rechercher() {
        ArrayList<PlanningActivity> planningList = new ArrayList<>();
        String req = "SELECT * FROM planningactivity";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                planningList.add(new PlanningActivity(
                        rs.getInt("idActivity"),
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

    // Méthode pour récupérer les salles associées à une activité
    public List<SalleSportif> getSallesByActivity(int idActivity) {
        List<SalleSportif> sallesList = new ArrayList<>();
        String query = "SELECT s.* FROM sallesportif s "
                + "JOIN planningactivity p ON s.idSalle = p.idSalle "
                + "WHERE p.idActivity = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idActivity);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SalleSportif salle = new SalleSportif(
                        rs.getInt("idSalle"),
                        rs.getString("nomSalle"),
                        rs.getString("addresseSalle"),
                        rs.getTime("horaireOuverture").toLocalTime(),
                        rs.getTime("horaireFermeture").toLocalTime(),
                        rs.getInt("capacity")
                );
                sallesList.add(salle);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des salles pour l'activité: " + e.getMessage());
        }
        return sallesList;
    }

}
