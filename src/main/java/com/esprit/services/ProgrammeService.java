package com.esprit.services;

import com.esprit.models.Programme;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammeService  {

    private Connection connection = DataSource.getInstance().getConnection();

    public List<Integer> getAllProgrammeIds() {
        List<Integer> programmeIds = new ArrayList<>();
        String query = "SELECT id FROM programme";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                programmeIds.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return programmeIds;
    }

    public List<Integer> getAllActivityIds() {
        List<Integer> programmeIds = new ArrayList<>();
        String query = "SELECT idActivity FROM activity";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                programmeIds.add(rs.getInt("idActivity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return programmeIds;
    }

    /**
     * Ajoute un programme à la base de données.
     */

    public void ajouter(Programme programme) {
        String req = "INSERT INTO Programme (prix, description, coach_id,lieu) VALUES ( ?, ?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {


            pst.setDouble(1, programme.getPrix());
            pst.setString(2, programme.getDescription());
            pst.setInt(3, programme.getCoach_id());

            pst.setString(4, programme.getLieu());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                programme.setId(rs.getInt(1)); // Mettre à jour l'ID de l'objet
            }

            System.out.println("✅ Programme ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    /**
     * Modifie un programme existant.
     */

    public boolean modifier(Programme programme) {
        String req = "UPDATE Programme SET prix = ?, description = ?, coach_id = ?, lieu=? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pst.setDouble(1, programme.getPrix());
            pst.setString(2, programme.getDescription());
            pst.setInt(3, programme.getCoach_id());
            pst.setString(4, programme.getLieu());
            pst.setInt(5, programme.getId());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                programme.setId(rs.getInt(1)); // Mettre à jour l'ID de l'objet
            }
            System.out.println("Programme modifiée");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;}

    /**
     * Supprime un programme par son ID.
     *
     * @return
     */

    public boolean supprimer(int id) {
        String req = "DELETE FROM Programme WHERE id = ?";
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
                        rs.getInt("coach_id"),
                        rs.getString("lieu")
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
        String query = "SELECT description FROM Programme"; // Récupérer uniquement la colonne "description"

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                descriptions.add(resultSet.getString("description")); // Ajouter la description à la liste
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return descriptions;
    }
    public int countProgrammesAbove6() {
        String query = "SELECT COUNT(*) FROM programme WHERE prix > 6";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countProgrammesBelowOrEqual6() {
        String query = "SELECT COUNT(*) FROM programme WHERE prix <= 6";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

