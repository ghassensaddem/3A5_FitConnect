package com.esprit.services;

import com.esprit.models.CategorieEquipement;
import com.esprit.models.Commande_Equipement;
import com.esprit.models.Equipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Commande_EquipementService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter une association commande-équipement
    public void ajouter(Commande_Equipement commandeEquipement) {
        String req = "INSERT INTO commande_equipement (commande_id, equipement_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commandeEquipement.getCommandeId());
            pst.setInt(2, commandeEquipement.getEquipementId());
            pst.setInt(3, commandeEquipement.getQuantite());
            pst.setDouble(4, commandeEquipement.getPrixUnitaire());

            pst.executeUpdate();
            System.out.println("Association commande-équipement ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'association commande-équipement : " + e.getMessage());
        }
    }

    // Modifier une association commande-équipement
    public void modifier(Commande_Equipement commandeEquipement) {
        String req = "UPDATE commande_equipement SET commande_id=?, equipement_id=?, quantite=?, prix_unitaire=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commandeEquipement.getCommandeId());
            pst.setInt(2, commandeEquipement.getEquipementId());
            pst.setInt(3, commandeEquipement.getQuantite());
            pst.setDouble(4, commandeEquipement.getPrixUnitaire());
            pst.setInt(5, commandeEquipement.getId());

            pst.executeUpdate();
            System.out.println("Association commande-équipement modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'association commande-équipement : " + e.getMessage());
        }
    }

    // Supprimer une association commande-équipement
    public void supprimer(Commande_Equipement commandeEquipement) {
        String req = "DELETE FROM commande_equipement WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commandeEquipement.getId());
            pst.executeUpdate();
            System.out.println("Association commande-équipement supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'association commande-équipement : " + e.getMessage());
        }
    }

    // Récupérer toutes les associations commande-équipement
    public List<Commande_Equipement> rechercher() {
        List<Commande_Equipement> commandeEquipements = new ArrayList<>();
        String req = "SELECT * FROM commande_equipement";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Commande_Equipement commandeEquipement = new Commande_Equipement(
                        rs.getInt("id"),
                        rs.getInt("commande_id"),
                        rs.getInt("equipement_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire")
                );
                commandeEquipements.add(commandeEquipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des associations commande-équipement : " + e.getMessage());
        }

        return commandeEquipements;
    }

    // Récupérer les associations pour une commande spécifique
    public List<Commande_Equipement> rechercherParCommandeId(int commandeId) {
        List<Commande_Equipement> commandeEquipements = new ArrayList<>();
        String req = "SELECT * FROM commande_equipement WHERE commande_id=?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commandeId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Commande_Equipement commandeEquipement = new Commande_Equipement(
                        rs.getInt("id"),
                        rs.getInt("commande_id"),
                        rs.getInt("equipement_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire")
                );
                commandeEquipements.add(commandeEquipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des associations pour la commande : " + e.getMessage());
        }

        return commandeEquipements;
    }

    // Récupérer les associations pour un équipement spécifique
    public List<Commande_Equipement> rechercherParEquipementId(int equipementId) {
        List<Commande_Equipement> commandeEquipements = new ArrayList<>();
        String req = "SELECT * FROM commande_equipement WHERE equipement_id=?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, equipementId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Commande_Equipement commandeEquipement = new Commande_Equipement(
                        rs.getInt("id"),
                        rs.getInt("commande_id"),
                        rs.getInt("equipement_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire")
                );
                commandeEquipements.add(commandeEquipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des associations pour l'équipement : " + e.getMessage());
        }

        return commandeEquipements;
    }
    /*public List<Equipement> getEquipementsParCommandeId(int commandeId) {
        List<Equipement> equipements = new ArrayList<>();
        String req = "SELECT e.*, c.nom AS categorie_nom " +
                "FROM equipement e " +
                "LEFT JOIN categorie_equipement c ON e.categorie_id = c.id " +
                "WHERE e.id IN ( " +
                "    SELECT equipement_id " +
                "    FROM commande_equipement " +
                "    WHERE commande_id = ? " +
                ")";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commandeId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Récupérer l'ID et le nom de la catégorie
                int categorieId = rs.getInt("categorie_id");
                String categorieNom = rs.getString("categorie_nom");

                // Créer l'objet CategorieEquipement
                CategorieEquipement categorie = null;
                if (!rs.wasNull()) { // Vérifier si la catégorie existe
                    categorie = new CategorieEquipement(categorieId, categorieNom);
                }

                // Créer l'objet Equipement avec la catégorie
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        categorie // Ajouter la catégorie
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements de la commande : " + e.getMessage());
        }

        return equipements;
    }
*/



}