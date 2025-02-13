package com.esprit.services;

import com.esprit.models.Equipement;
import com.esprit.models.CategorieEquipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter un équipement
    public void ajouter(Equipement equipement) {
        String req = "INSERT INTO equipement (nom, description, prix, quantite_stock, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());


            pst.executeUpdate();
            System.out.println("Équipement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'équipement : " + e.getMessage());
        }
    }

    // Modifier un équipement
    public void modifier(Equipement equipement) {
        String req = "UPDATE equipement SET nom=?, description=?, prix=?, quantite_stock=?, categorie_id=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());
            pst.setInt(6, equipement.getId());
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
        String req = "SELECT e.*, c.nom AS categorie_nom FROM equipement e LEFT JOIN categorie_equipement c ON e.categorie_id = c.id";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                // Récupération de l'ID de la catégorie
                int categorieId = rs.getInt("categorie_id");




                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        categorieId
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }




}
