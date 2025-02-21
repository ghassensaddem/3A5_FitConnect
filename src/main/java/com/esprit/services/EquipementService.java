package com.esprit.services;

import com.esprit.models.Equipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter un équipement
    public void ajouter(Equipement equipement) {
        String req = "INSERT INTO equipement (nom, description, prix, quantite_stock, categorie_id,image) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());
            pst.setString(6, equipement.getImage());
            pst.executeUpdate();
            System.out.println("Équipement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'équipement : " + e.getMessage());
        }
    }

    // Modifier un équipement
    public void modifier(Equipement equipement) {
        String req = "UPDATE equipement SET nom=?, description=?, prix=?, quantite_stock=?, categorie_id=?, image=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());
            pst.setString(6, equipement.getImage());
            pst.setInt(7, equipement.getId());
            pst.executeUpdate();
            System.out.println("Équipement modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'équipement : " + e.getMessage());
        }
    }

    // Supprimer un équipement
    public void supprimer(Equipement e1) {
        String req = "DELETE FROM equipement WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, e1.getId());
            pst.executeUpdate();
            System.out.println("Équipement supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'équipement : " + e.getMessage());
        }
    }

    public List<Equipement> rechercher() {
        List<Equipement> equipements = new ArrayList<>();
        String req = "SELECT * FROM equipement";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("categorie_id"),
                        rs.getString("image")
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }
    public List<Equipement> rechercherParId(int id) {
        List<Equipement> equipements = new ArrayList<>();
        String req = "SELECT * FROM equipement WHERE categorie_id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("categorie_id"),
                        rs.getString("image")
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }





}