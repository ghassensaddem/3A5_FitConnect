package com.esprit.services;

import com.esprit.models.Programme;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammeService  {

    private Connection connection = DataSource.getInstance().getConnection();

    /**
     * Ajoute un programme à la base de données.
     */

    public void ajouter(Programme programme) {
        String req = "INSERT INTO programme (prix, description, coach_id) VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDouble(1, programme.getPrix());
            pst.setString(2, programme.getDescription());
            pst.setInt(3, programme.getCoach_id());
            pst.executeUpdate();
            System.out.println("✅ Programme ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    /**
     * Modifie un programme existant.
     */

    public boolean modifier(Programme programme) {
        String req = "UPDATE programme SET prix = ?, description = ?, coach_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDouble(1, programme.getPrix());
            pst.setString(2, programme.getDescription());
            pst.setInt(3, programme.getCoach_id());
            pst.setInt(4, programme.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Programme modifié avec succès.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un programme par son ID.
     *
     * @return
     */

    public boolean supprimer(int id) {
        String req = "DELETE FROM programme WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Programme supprimé avec succès.");
                return true;
            } else {
                System.out.println("⚠️ Aucun programme trouvé avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère tous les programmes.
     */

    public ArrayList<Programme> rechercher() {
        ArrayList<Programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                programmes.add(new Programme(
                        rs.getInt("id"),
                        rs.getDouble("prix"),
                        rs.getString("description"),
                        rs.getInt("coach_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }
        return programmes;
    }

    /**
     * Alias pour `rechercher()`, pour éviter les doublons de méthode.
     */
    public List<String> recupererDescriptions() {
        List<String> descriptions = new ArrayList<>();
        String query = "SELECT description FROM programme"; // Récupérer uniquement la colonne "description"

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                descriptions.add(resultSet.getString("description")); // Ajouter la description à la liste
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return descriptions;
    }}

