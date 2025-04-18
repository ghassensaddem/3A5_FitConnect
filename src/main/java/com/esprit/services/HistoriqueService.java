package com.esprit.services;

import com.esprit.models.Historique;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueService {
    private Connection conn;

    public HistoriqueService() {
        conn = DataSource.getInstance().getConnection();
    }

    public List<Historique> getAllHistorique() {
        List<Historique> historiqueList = new ArrayList<>();
        String query = "SELECT * FROM historique ORDER BY date DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Historique h = new Historique(
                        rs.getInt("id"),
                        rs.getString("action"),
                        rs.getString("entite"),
                        rs.getTimestamp("date"),
                        rs.getString("details")
                );
                historiqueList.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiqueList;
    }

    public void ajouterHistorique(String action, String entite, String details) {
        String query = "INSERT INTO historique (action, entite, details) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, action);
            pstmt.setString(2, entite);
            pstmt.setString(3, details);

            pstmt.executeUpdate();
            System.out.println("Historique ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifierHistorique(int id, String action, String entite, String details) {
        String query = "UPDATE historique SET action = ?, entite = ?, details = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, action);
            pstmt.setString(2, entite);
            pstmt.setString(3, details);
            pstmt.setInt(4, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Historique modifié avec succès !");
            } else {
                System.out.println("Aucun historique trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}