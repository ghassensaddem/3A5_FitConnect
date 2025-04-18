package com.esprit.services;

import com.esprit.models.application;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class applicationService  {
    private Connection connection = DataSource.getInstance().getConnection();

    // ✅ Correction : enregistrer en récupérant l'ID généré
    public int enregistrer(application application) {
        String sql = "INSERT INTO application (dateDebut, dateFin, idProgramme, idClient) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(application.getDateDebut()));
            stmt.setDate(2, Date.valueOf(application.getDateFin()));
            stmt.setInt(3, application.getIdProgramme());
            stmt.setInt(4, application.getIdClient());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    application.setId(generatedId); // Mettre à jour l'ID de l'objet
                }
                System.out.println("Application ajoutée avec succès (ID: " + generatedId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement: " + e.getMessage());
        }
        return generatedId;
    }


    public void ajouter(application application) {
        enregistrer(application); // Utilisation de la méthode corrigée
    }


    public boolean modifier(application application) {
        String req = "UPDATE application SET dateDebut = ?, dateFin = ?, idProgramme = ?, idClient = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDate(1, Date.valueOf(application.getDateDebut()));
            pst.setDate(2, Date.valueOf(application.getDateFin()));
            pst.setInt(3, application.getIdProgramme());
            pst.setInt(4, application.getIdClient());
            pst.setInt(5, application.getId());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Application modifiée avec succès (ID: " + application.getId() + ")");
                return true;
            } else {
                System.out.println("Aucune modification effectuée, vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        String req = "DELETE FROM application WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Application supprimée avec succès (ID: " + id + ")");
                return true;
            } else {
                System.out.println("⚠️ Aucune application trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
        return false;
    }


    public ArrayList<application> rechercher() {
        ArrayList<application> applications = new ArrayList<>();
        String req = "SELECT * FROM application";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                applications.add(new application(
                        rs.getInt("id"),
                        rs.getDate("dateDebut").toLocalDate(),
                        rs.getDate("dateFin").toLocalDate(),
                        rs.getInt("idProgramme"),
                        rs.getInt("idClient")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des applications: " + e.getMessage());
        }
        return applications;
    }
}
