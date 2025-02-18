package com.esprit.services;

import com.esprit.models.application;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class applicationService implements iService<application> {
    private Connection connection = DataSource.getInstance().getConnection();


    public void enregistrer(application application) {
        String sql = "INSERT INTO application (dateDebut, dateFin, idProgramme, idClient) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(application.getDateDebut()));
            stmt.setDate(2, Date.valueOf(application.getDateFin()));
            stmt.setInt(3, application.getIdProgramme());
            stmt.setInt(4, application.getIdClient());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Application ajoutée avec succès");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'enregistrement dans la base de données: " + e.getMessage());
        }
    }

    @Override
    public void ajouter(application application) {
        String req = "INSERT INTO application (dateDebut, dateFin, idProgramme, idClient) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDate(1, Date.valueOf(application.getDateDebut()));
            pst.setDate(2, Date.valueOf(application.getDateFin()));
            pst.setInt(3, application.getIdProgramme());
            pst.setInt(4, application.getIdClient());

            pst.executeUpdate();
            System.out.println("Application ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean modifier(application application) {
        String req = "UPDATE application SET dateDebut = ?, dateFin = ?, idProgramme = ?, idClient = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDate(1, Date.valueOf(application.getDateDebut()));
            pst.setDate(2, Date.valueOf(application.getDateFin()));
            pst.setInt(3, application.getIdProgramme());
            pst.setInt(4, application.getIdClient());
            pst.setInt(5, application.getId());
            pst.executeUpdate();
            System.out.println("Application modifiée");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;}

    public boolean supprimer(int id) {
        String req = "DELETE FROM application WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);  // Utiliser directement l'ID
            int rowsAffected = pst.executeUpdate();  // Vérifier combien de lignes ont été affectées
            if (rowsAffected > 0) {
                System.out.println("Application supprimée avec succès");
                return true; // Retourner true si l'application a été supprimée
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Aucun programme trouvé avec cet ID.");
        }
        return false; // Retourner false si aucune ligne n'a été affectée
    }


    @Override
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
            System.out.println(e.getMessage());
        }

        return applications;
    }
}
